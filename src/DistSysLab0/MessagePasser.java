package distSysLab0;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

import distSysLab0.RuleBean.RuleAction;

public class MessagePasser {
    private static MessagePasser instance;
    private static Logger logger = Logger.getLogger(MessagePasser.class);
    private ConfigParser configParser;

    private LinkedBlockingDeque<Message> sendQueue = new LinkedBlockingDeque<Message>();
    private LinkedBlockingDeque<Message> delayQueue = new LinkedBlockingDeque<Message>();
    private LinkedBlockingDeque<Message> recvQueue = new LinkedBlockingDeque<Message>();
    private HashMap<String, NodeBean> nodeList = new HashMap<String, NodeBean>();
    private ArrayList<RuleBean> sendRules = new ArrayList<RuleBean>();
    private ArrayList<RuleBean> recvRules = new ArrayList<RuleBean>();

    private String configFile;
    private String localName;
    private String MD5Last;
    private int curSeqNum;

    private ListenerThread listener;
    private SenderThread sender;

    /**
     * Actual constructor for MessagePasser
     * 
     * @param configFile
     * @param localName
     */
    private MessagePasser(String configFile, String localName)
            throws UnknownHostException {
        this.localName = localName;
        this.configFile = configFile;
        this.curSeqNum = 0;

        configParser = new ConfigParser(configFile);
        nodeList = configParser.readConfig();
        sendRules = configParser.readSendRules();
        recvRules = configParser.readRecvRules();
        MD5Last = getMD5Checksum(configFile);

        if(nodeList.get(localName) == null) {
            logger.error("The local name is incorrect.");
            System.exit(0);
        }
        else {
            listener = new ListenerThread(nodeList.get(localName).getPort(), recvQueue);
            sender = new SenderThread(sendQueue, delayQueue, nodeList);;
        }

        logger.debug(this.toString());
    }

    /**
     * Initialization for receive thread.
     */
    public synchronized void startListener() {
        Thread thread = new Thread(this.listener); 
        thread.start();
    }

    /**
     * Initialization for send thread.
     */
    public synchronized void startSender() {
        Thread thread = new Thread(this.sender); 
        thread.start();
    }

    /**
     * Singleton constructor for MessagePasser
     * 
     * @param configuration_filename
     * @param local_name
     */
    public static synchronized MessagePasser getInstance(
                                                         String configuration_filename, String local_name)
                                                                 throws UnknownHostException {
        if (instance == null) {
            instance = new MessagePasser(configuration_filename, local_name);
        }
        return instance;
    }

    /**
     * Return existed instance of MessagePasser
     * 
     * @return instance
     */
    public static MessagePasser getInstance() {
        return instance;
    }

    /**
     * Generate checksum array of a input file.
     */
    public static byte[] createChecksum(String filename) {
        InputStream fis;
        MessageDigest complete = null;

        try {
            fis = new FileInputStream(filename);

            byte[] buffer = new byte[1024];
            complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
        }
        catch (IOException | NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return complete.digest();
    }

    /**
     * Generate MD5 value of a input file.
     * @param filename The input file.
     * @return The MD5 value of input file.
     */
    public synchronized static String getMD5Checksum(String filename) {
        byte[] b;
        String result = "";
        b = createChecksum(filename);

        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16)
                    .substring(1);
        }

        return result;
    }

    /**
     * Send a message.
     * 
     * @param message The message need to be sent.
     */
    public void send(Message message) {
        // Set source and seq of the massage
        message.setSrc(localName);
        message.setSeqNum(curSeqNum++);

        // Check if the configuration file has been changed.
        String MD5 = getMD5Checksum(configFile);
        if (!MD5.equals(MD5Last)) {
            sendRules = configParser.readSendRules();
            recvRules = configParser.readRecvRules();
            MD5Last = MD5;
        }

        // Try to match a rule from the send rule list.
        RuleAction action = RuleAction.NONE;
        for (RuleBean rule : sendRules) {
            if (rule.isMatch(message)) {
                action = rule.getAction();
            }
        }	

        // Do action according the matched rule's type.
        switch (action) {
        case DROP:
            // Just drop this message.
            break;

        case DUPLICATE:
            // Add this message into sendQueue.
            sendQueue.add(message);
            // Add a duplicate message into sendQueue.
            message.setDuplicate(true);
            sendQueue.add(message);
            break;

        case DELAY:
            // Add this message into delayQueue
            delayQueue.add(message);
            break;

        case NONE:
            // Add this message into sendQueue
            sendQueue.add(message);
        } 	
    }

    /**
     * Deliver message from the input queue
     * 
     * @return A message
     */
    public Message receive() {
        // Check if the configuration file has been changed.
        String MD5 = getMD5Checksum(configFile);
        if (!MD5.equals(MD5Last)) {
            sendRules = configParser.readSendRules();
            recvRules = configParser.readRecvRules();
            MD5Last = MD5;
        }

        ArrayList<Message> messages = new ArrayList<Message>();
        synchronized (recvQueue) {
            if (!recvQueue.isEmpty()) {

                messages.add(recvQueue.poll());
            }
        }
        return messages.remove(0);
    }

    /**
     * Do the termination work.
     */
    public void teminate() throws IOException {
        listener.teminate();

        //sender.teminate();
    }

    public HashMap<String, NodeBean> getNodeList() {
        return nodeList;
    }

    @Override
    public String toString() {
        return "MessagePasser [configFile=" + configFile + ", localName=" + localName
                + ", listenSocket=" + listener.toString() + "]";
    }
}