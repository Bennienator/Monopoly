package de.seben.monopoly.server;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnection extends Thread{

    private ServerSocket server;
    private Socket client;

    public ClientConnection(ServerSocket server){
        this.server = server;
    }

    public void run(){
        try {
            while (true) {
                Socket socket = server.accept();
                if(socket != null){
                    client = socket;
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(new Command(CommandType.ACCEPT));
                    oos.close();
                    System.out.println("'ACCEPT' send");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
