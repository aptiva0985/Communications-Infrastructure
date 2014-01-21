package distSysLab0;

import java.io.*;
import java.net.*;

public class Receiver implements Runnable {
	private int servPort;
	private int recvMsgSize;
	public Receiver() {}	
	public Receiver(int port) {
		this.servPort = port;
	}
	@SuppressWarnings("resource")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte [] receiveBuf = new byte[2014];
		try {
			ServerSocket servSock=new ServerSocket(this.servPort);
			while(true) {
				Socket clntSock=servSock.accept();
				Message message = null;
				SocketAddress clientAddress=clntSock.getRemoteSocketAddress();
				System.out.println("Handling client at "+clientAddress);
				ObjectInputStream in = new ObjectInputStream(
						new BufferedInputStream(clntSock.getInputStream()));
				if((message = (Message)(in.readObject())) != null){
					
					//out.write(receiveBuf, 0, recvMsgSize);
					System.out.println((String)message.getData());								
				}
				clntSock.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}	
}
