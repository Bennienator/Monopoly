package de.seben.monopoly.server;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnection extends Thread{

    private ServerSocket server;
    private Socket socket;

    public ClientConnection(ServerSocket server){
        this.server = server;
    }

    public void run(){
        try {
            while (socket == null) {
                socket = server.accept();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object in;
                if ((in = ois.readObject()) != null) {
                    Command input = (Command) in;
                    CommandType cmdType = input.getCmdType();
                    switch (cmdType) {
                        case DISCONNECT:
                            break;
                        case BUILD_HOUSE:
                            break;
                        case END_OF_ROUND:
                            break;
                        //USW
                    }
                }
            }catch (EOFException e){
                try {
                    sleep(10);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

}
