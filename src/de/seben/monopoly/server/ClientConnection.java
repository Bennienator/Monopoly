package de.seben.monopoly.server;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new Command(CommandType.CHAT, "Fucked you", "to"));
            oos.close();
            System.out.println("'ACCEPT' send");
            sleep(2000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new Command(CommandType.ACCEPT));
            oos.close();
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

}
