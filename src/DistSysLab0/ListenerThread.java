package distSysLab0;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

public class ListenerThread implements Runnable {
    private static Logger logger = Logger.getLogger(ListenerThread.class);

    private int port;
    private LinkedBlockingDeque<Message> recvQueue;
    private ServerSocket listenSocket;

    public ListenerThread(int port, LinkedBlockingDeque<Message> recvQueue) {
        this.port = port;
        this.recvQueue = recvQueue;
    }

    @Override
    public void run() {
        try {
            listenSocket = new ServerSocket(this.port);
            while(true) {
                Socket socket = listenSocket.accept();
                logger.info("Handling client at " + socket.getRemoteSocketAddress());
                System.out.println("Handling client at " + socket.getRemoteSocketAddress());
                // Create a new thread for new incoming connection.
                Thread thread = new Thread(new ReceiverThread(socket, recvQueue));
                thread.start();
            }
        }
        catch (IOException e) {
            logger.error("ERROR: Server socket error");
            System.err.println("ERROR: server Socket error");
        }
    }

    public void teminate() throws IOException {
        listenSocket.close();
    }
    
    @Override
    public String toString() {
        return "Listener [port=" + port + "]";
    }
}
