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
    private int id;
    private User user;

    public ClientConnection(ServerSocket server){
        this.server = server;
    }

    public void run(){
        try {
            while (socket == null) {
                socket = server.accept();
                ClientController.getInstance().preRegisterPlayer(this);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        while(socket != null){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Command in = (Command) ois.readObject();
                ClientController.getInstance().analyseIncommingCommand(in);
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
        System.out.println("Client disconnected");
        ClientController.getInstance().disconnect(this);
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
    public int getID(){
        return this.id;
    }
    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return this.user;
    }

}
