package distSysLab0;

import java.net.UnknownHostException;

public class Simulator {

	/**
	 * For test.
	 */
	public static void main(String[] args) {
		try {
			MessagePasser messagePasser = MessagePasser.getInstance("config.yaml", "jing");
			messagePasser.startReceiver();
			Message m = new Message("jing", Message.MessageKind.ACK, "Hello from Jacky");
			messagePasser.send(m);
			//System.out.println("Send");
				
		} catch (UnknownHostException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
