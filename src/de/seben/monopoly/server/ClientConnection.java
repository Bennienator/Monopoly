package de.seben.monopoly.server;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        }catch (IOException e){
            e.printStackTrace();
        }
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object in;
                if ((in = ois.readObject()) != null) {
                    ClientController.getInstance().analyseCommand((Command) in);
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

    public void sendCommand(Command output){
        try {
            if (socket != null) {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
