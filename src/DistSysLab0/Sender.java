package distSysLab0;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class Sender implements Runnable{
	private LinkedBlockingDeque<Message> sendQueue;
	private LinkedBlockingDeque<Message> delayQueue;
	private HashMap<String, NodeBean> nodeList;
	public Sender() {}	
	public Sender(LinkedBlockingDeque<Message> sendQueue, 
					LinkedBlockingDeque<Message> delayQueue, 
					HashMap<String, NodeBean> nodeList) {
		this.sendQueue = sendQueue;
		this.delayQueue = delayQueue;
		this.nodeList = nodeList;
	}
	@SuppressWarnings("resource")
	@Override
	public void run() {
		
		
		// if there is one non-delay message, put all delay message into sendQueue
		if(!sendQueue.isEmpty()) { 
			while(!delayQueue.isEmpty()) {
				sendQueue.add(delayQueue.pollFirst());
			}
		}
		while(!sendQueue.isEmpty()) {
			// TODO send all message in sendQueue
			Message message = sendQueue.pollFirst();
			String serverName = message.getDest();
			String servIp = nodeList.get(serverName).getIp();
			int servPort = nodeList.get(serverName).getPort();
			try {
				Socket socket = new Socket(servIp, servPort);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						new BufferedOutputStream(socket.getOutputStream()));
				objectOutputStream.writeObject(message);
				objectOutputStream.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
