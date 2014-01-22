package distSysLab0;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

public class ReceiverThread implements Runnable {
    private static Logger logger = Logger.getLogger(ReceiverThread.class);

    private Socket socket;
    private LinkedBlockingDeque<Message> recvQueue;

    public ReceiverThread(Socket socket, LinkedBlockingDeque<Message> recvQueue) {
        this.socket = socket;
        this.recvQueue = recvQueue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Message message = null;

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                if((message = (Message) (in.readObject())) != null) {
                    // Try to match a rule and act corresponding
                    // TODO The match procedure should be in the listener thread
                    // out.write(receiveBuf, 0, recvMsgSize);
                    System.out.println((String) message.getData());
                    logger.info((String) message.getData());

                    synchronized(recvQueue) {
                        recvQueue.add(message);
                    }
                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void teminate() throws IOException {
        socket.close();
    }
}
