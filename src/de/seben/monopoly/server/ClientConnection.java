package de.seben.monopoly.server;

import de.seben.monopoly.events.ServerCommandRecieveEvent;
import de.seben.monopoly.events.UserQuitEvent;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection extends Thread{

    private ServerSocket server;
    private Socket socket;
    private int id;
    private User user;
    private ClientController controller;

    public ClientConnection(ServerSocket server, int id){
        Monopoly.debug("Created instance");
        this.server = server;
        this.id = id;
        this.controller = ClientController.getInstance();
    }

    public void run(){
        Monopoly.debug("Waiting...");
        try {
            while (socket == null ) {
                socket = server.accept();
            }
            Monopoly.debug("(" + this.id + ") Socket accepted");
            ClientController.getInstance().preRegisterPlayer(this);
            Server.getInstance().getController().createNewClientConnection(this.server);
        }catch (IOException e){
            e.printStackTrace();
        }
        while(socket.isConnected()){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Command in = (Command) ois.readObject();
                Server.getInstance().getEvents().executeEvent(new ServerCommandRecieveEvent(this.user, in));
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
        Server.getInstance().getEvents().executeEvent(new UserQuitEvent(this));
        Monopoly.debug("Disconnected");
    }

    public void sendCommand(Command output){
        try {
            if (socket != null) {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(output);
                oos.close();
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
