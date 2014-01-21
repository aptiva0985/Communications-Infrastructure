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
			while(true){
				Socket clntSock=servSock.accept();
				SocketAddress clientAddress=clntSock.getRemoteSocketAddress();
				System.out.println("Handling client at "+clientAddress);
				InputStream in=clntSock.getInputStream();
				while((recvMsgSize=in.read(receiveBuf))!=-1){
					for(byte b : receiveBuf) {
						char c = (char)b;
						System.out.println(c);	
					}
					//out.write(receiveBuf, 0, recvMsgSize);
					System.out.println(recvMsgSize);
								
				}
				clntSock.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}	
}
