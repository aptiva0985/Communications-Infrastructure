package distSysLab0;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import distSysLab0.Message.MessageKind;
import distSysLab0.RuleBean.RuleAction;

public class MessagePasser {
	private static MessagePasser instance;
	private static Logger logger = Logger.getLogger(MessagePasser.class);
	private ConfigParser configParser;

	private LinkedBlockingDeque<Message> sendQueue = new LinkedBlockingDeque<Message>();
	private LinkedBlockingDeque<Message> recvQueue = new LinkedBlockingDeque<Message>();
	private HashMap<String, NodeBean> nodeList = new HashMap<String, NodeBean>();
	private ArrayList<RuleBean> sendRules = new ArrayList<RuleBean>();
	private ArrayList<RuleBean> recvRules = new ArrayList<RuleBean>();

	private String configFile;
	private String localName;
	private int port;
	private String MD5Last;
	private int curId;
	
	private Receiver receiver;

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
		this.curId = 0;

		configParser = new ConfigParser(configFile);
		nodeList = configParser.readConfig();
		MD5Last = getMD5Checksum(configFile);
		
		int servPort = nodeList.get(localName).getPort();
		receiver = new Receiver(servPort);

		logger.debug(this.toString());
	}
	
	public synchronized void startReceiver() {
		Thread thread = new Thread(this.receiver); 
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

	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public synchronized static String getMD5Checksum(String filename) {
		byte[] b;
		String result = "";
		try {
			b = createChecksum(filename);

			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16)
						.substring(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Send a message.
	 * 
	 * @param message
	 *            The message need to be sent.
	 */
	public void send(Message message) {
		String MD5 = getMD5Checksum(configFile);
		if (!MD5.equals(MD5Last)) {
			configParser.readSendRules();
			MD5Last = MD5;
		}

		message.setSeqNum(curId++);

		RuleAction action = RuleAction.NONE;
		for (RuleBean rule : sendRules) {
			if (rule.isMatch(message)) {
				action = rule.getAction();
			}
		}

		switch (action) {
		default:
			String serverName = message.getDest();
			String servIp = nodeList.get(serverName).getIp();
			int servPort = nodeList.get(serverName).getPort();
			Socket socket;
			try {
				socket = new Socket(servIp, servPort);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
														new BufferedOutputStream(socket.getOutputStream()));
				objectOutputStream.writeObject(message);
				objectOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Peek message list from receive queue.
	 * 
	 * @return A message list.
	 */
	public ArrayList<Message> receive() {
		String MD5 = getMD5Checksum(configFile);
		if (!MD5.equals(MD5Last)) {
			configParser.readRecvRules();
			MD5Last = MD5;

		}
		ArrayList<Message> receiveList = new ArrayList<Message>();
		synchronized (recvQueue) {
			while (!recvQueue.isEmpty()) {
				// Try to match a rule and act corresponding
				// TODO The match procedure should be in the listener thread
				receiveList.add(recvQueue.poll());
			}
		}
		return receiveList;
	}

	/**
	 * For test.
	 */
	public static void main(String[] args) {
		try {
			MessagePasser messagePasser = MessagePasser.getInstance("config.yaml", "jing");
			messagePasser.startReceiver();
			Message m = new Message("jing", Message.MessageKind.ACK, "NNNNNNN------NNNNNNN");
			messagePasser.send(m);		
			System.out.println("come here!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "";
	}
}
