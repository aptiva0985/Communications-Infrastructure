package DistSysLab0;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.security.MessageDigest;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

public class MessagePasser {
    private static MessagePasser instance;
    private static Logger logger = Logger.getLogger(MessagePasser.class);
    
    private Queue<Message> sendQueue = new LinkedList<Message>();
    private Queue<Message> recvQueue = new LinkedList<Message>();
    
    private String configFile;
    private String localName;
    private int port;
    
    /**
     * Actual constructor for MessagePasser
     * @param configFile
     * @param localName
     */
    private MessagePasser(String configFile, String localName) {
        this.localName = localName;
        this.configFile = configFile;
        this.port = 0;
        this.init();    
        logger.debug(this.toString());
    }
    
    /**
     * Singleton constructor for MessagePasser
     * @param configuration_filename
     * @param local_name
     */
    public static synchronized MessagePasser getInstance(String configuration_filename, String local_name) {
        if (instance == null) {
            instance = new MessagePasser(configuration_filename, local_name);
        }
        return instance;
    }
    
    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

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
		try {
			b = createChecksum(filename);
			String result = "";

	        for (int i=0; i < b.length; i++) {
	            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	        }
	        return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return " ";
		}        
    }
    
    /**
     * Return existed instance of MessagePasser
     * @return instance
     */
    public static MessagePasser getInstance() {
        return instance;
    }

    /**
     * Peek a message from receive queue.
     * @return A message.
     */
    public synchronized Message receive() {
        Message msg = recvQueue.peek();
        if(msg != null) {
            recvQueue.remove();
            return msg;
        }
        return null;
    }
    
    public synchronized void init() {
        
    }

    /**
     * For test.
     */
    public static void main(String[] args) {
    	Yaml yaml;
    	File optionFile;
    	FileReader fr;
    	BufferedReader br;
    	LinkedHashMap<String, String> configMap = null;
    	String MD5Last = "";
    	
    	try {
    		String configuration_filename = "config.yaml";
    		//before sending and receiving message, 
    		String MD5 = getMD5Checksum(configuration_filename);
    		
	        //check whether config file has been changed
    		//if changed, re-reads the file and builds configure map
    		if(!MD5.equals(MD5Last)) { 
				yaml = new Yaml();
				optionFile = new File(configuration_filename);
				fr = new FileReader(optionFile);
	            br = new BufferedReader(fr);
	            configMap = (LinkedHashMap<String, String>)yaml.load(br);
	            MD5Last = MD5;
    		}
            
            //sending and receiving message according to configMap
    		System.out.println(MD5);
            System.out.println(configMap);
		}
		catch (Exception e) {
			System.out.println("File Read Error: " + e.getMessage());
		}  
    }
    
    @Override
    public String toString() {
        return "";
    }
}
