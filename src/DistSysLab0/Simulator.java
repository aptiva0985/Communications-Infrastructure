package distSysLab0;

import java.io.IOException;
import java.net.UnknownHostException;

public class Simulator {
	static public UserInputThread uiThread;
	
	public static void startUIThread() {
		uiThread = new UserInputThread();
        Thread thread = new Thread(uiThread); 
        thread.start();
    }
	/**
	 * For test.
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		try {
			MessagePasser messagePasser = MessagePasser.getInstance("config.yaml", "jing");
			if(false)
				messagePasser.startListener();
			else{				
				messagePasser.startSender();		
				startUIThread();
		        //messagePasser.teminate();
			}
			
				
		} catch (UnknownHostException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
