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
    private String name;

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
        long start = System.currentTimeMillis();
        boolean shutdown;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ClientController.getInstance().analyseCommand((Command) ois.readObject());
        } catch (EOFException e) {
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Command in = (Command) ois.readObject();
                ClientController.getInstance().analyseCommand(in);
                if (in.getCmdType() == CommandType.DISCONNECT) return;
            }catch (EOFException e){
                try {
                    sleep(10);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }catch (IOException | ClassNotFoundException | NullPointerException e){
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
