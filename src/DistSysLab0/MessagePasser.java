package distSysLab0;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

public class MessagePasser {
    private static MessagePasser instance;
    private static Logger logger = Logger.getLogger(MessagePasser.class);
    private ConfigParser configParser;

    private Queue<Message> sendQueue = new LinkedList<Message>();
    private Queue<Message> recvQueue = new LinkedList<Message>();
    private HashMap<String, NodeBean> nodeList = new HashMap<String, NodeBean>();
    private ArrayList<RuleBean> sendRules = new ArrayList<RuleBean>();
    private ArrayList<RuleBean> recvRules = new ArrayList<RuleBean>();

    private String configFile;
    private String localName;
    private int port;
    private String MD5Last;

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
        this.port = 0;

        configParser = new ConfigParser(configFile);
        nodeList = configParser.readConfig();
        MD5Last = getMD5Checksum(configFile);

        logger.debug(this.toString());
    }

    /**
     * Singleton constructor for MessagePasser
     * 
     * @param configuration_filename
     * @param local_name
     */
    public static synchronized MessagePasser getInstance(String configuration_filename,
                                                         String local_name)
            throws UnknownHostException {
        if(instance == null) {
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

    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if(numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        }
        while(numRead != -1);

        fis.close();
        return complete.digest();
    }

    public synchronized static String getMD5Checksum(String filename) {
        byte[] b;
        String result = "";
        try {
            b = createChecksum(filename);

            for(int i = 0; i < b.length; i++) {
                result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Send a message.
     * @param message The message need to be sent.
     */
    public void send(Message message) {
        String MD5 = getMD5Checksum(configFile);
        if(!MD5.equals(MD5Last)) {
            configParser.readSendRules();
            MD5Last = MD5;
            
            
        }
    }

    /**
     * Peek a message from receive queue.
     * 
     * @return A message.
     */
    public synchronized Message receive() {
        String MD5 = getMD5Checksum(configFile);
        if(!MD5.equals(MD5Last)) {
            configParser.readRecvRules();
            MD5Last = MD5;
            
            
        }
        Message msg = recvQueue.peek();
        if(msg != null) {
            recvQueue.remove();
            return msg;
        }
        return null;
    }

    /**
     * For test.
     */
    public static void main(String[] args) {

    }

    @Override
    public String toString() {
        return "";
    }
}
