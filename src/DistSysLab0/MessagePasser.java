package DistSysLab0;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
    	try {
			String filename = "config.yaml";
	        //Reads the file and builds configure map
			Yaml yaml = new Yaml();
			File optionFile = new File(filename);
			FileReader fr = new FileReader(optionFile);
            BufferedReader br = new BufferedReader(fr);
            LinkedHashMap<String, String> list = (LinkedHashMap<String, String>) yaml.load(br);
            System.out.println(list);
		    
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
