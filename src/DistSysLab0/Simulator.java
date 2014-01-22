package distSysLab0;

import java.io.IOException;
import java.net.UnknownHostException;

public class Simulator {

	/**
	 * For test.
	 */
	public static void main(String[] args) throws IOException {
		try {
			MessagePasser messagePasser = MessagePasser.getInstance("config.yaml", "jing");
			if(false)
				messagePasser.startListener();
			else{
				messagePasser.startSender();
				
				Message m1 = new Message("jing", "ACK", "Hello from Jacky1");
				Message m2 = new Message("jing", "ACK", "Hello from Jacky2");
				Message m3 = new Message("jing", "ACK", "Hello from Jacky3");
				Message m4 = new Message("jing", "ACK", "Hello from Jacky4");
				Message m5 = new Message("jing", "ACK", "Hello from Jacky5");
				messagePasser.send(m1);
				messagePasser.send(m2);
				messagePasser.send(m3);
				messagePasser.send(m4);
				messagePasser.send(m5);
				
		        messagePasser.teminate();
			}
			
				
		} catch (UnknownHostException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
