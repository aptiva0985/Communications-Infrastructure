package distSysLab0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import distSysLab0.Message.MessageKind;

public class UserInputThread implements Runnable {
    private static Logger logger = Logger.getLogger(UserInputThread.class);

    @Override
    public void run() {
        MessagePasser msgPasser = MessagePasser.getInstance();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String command = in.readLine();
            while(!command.equals("quit")) {
                System.out.println("Enter command: (send/receive/status/exit)");
                
                if(command.equals("send")) {
                    System.out.println("Message dest:");
                    String dest = in.readLine();
                    while(!msgPasser.getNodeList().containsKey(dest)) {
                        System.out.println("Dest not existed, try again:");
                        dest = in.readLine();
                    }

                    System.out.println("Message kind (ack/lookup/deafult):");
                    String kind = in.readLine();
                    System.out.println("Message data:");
                    String data = in.readLine();

                    Message msg = new Message(dest, MessageKind.valueOf(kind), data);
                    msgPasser.send(msg);
                }
                else if(command.equals("receive")) {
                    Message msg = msgPasser.receive();
                    if(msg == null) {
                        System.out.println("Nothing received.");
                    }
                    else {
                        System.out.println("Received:");
                        System.out.println(msg.toString());
                    }
                }
                else if(command.equals("status")) {
                    System.out.println(msgPasser.toString());
                }
                else if(command.equals("exit")) {
                    msgPasser.teminate();
                    System.out.println("Exit.");
                    System.exit(0);
                }
                else {
                    System.out.println("Invalid command");
                }
            }
            
            msgPasser.teminate();
            System.out.println("Exit.");
            System.exit(0);
        }
        catch (IOException e) {
            logger.error("ERROR: Reader error");
            System.err.println("ERROR: Reader error");
            e.printStackTrace();
        }
    }
}
