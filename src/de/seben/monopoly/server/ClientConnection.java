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
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int id;
    private User user;
    private ClientController controller;
    private Command lastSendCommand;
    private static int amount;

    public ClientConnection(ServerSocket server){
        Monopoly.debug("Created instance");
        this.server = server;
        this.id = amount++;
        this.controller = ClientController.getInstance();
    }

    public void run(){
        Monopoly.debug("(" + this.id + ") Waiting...");
        try {
            while (socket == null || socket.isClosed()) {
                socket = server.accept();
            }
            Monopoly.debug("(" + this.id + ") Socket accepted");
            ClientController.getInstance().preRegisterPlayer(this);
            Server.getInstance().getController().createNewClientConnection(this.server);
            while(socket != null && !socket.isClosed()){
                    ois = new ObjectInputStream(socket.getInputStream());
                    Command in;
                    if((in = (Command) ois.readObject()) != null){
                        Server.getInstance().getEvents().executeEvent(new ServerCommandRecieveEvent(this.user, in));
                    }
            }
        }catch (SocketException e){
            if(!e.getMessage().equalsIgnoreCase("Socket closed")){
                Server.getInstance().getEvents().executeEvent(new UserQuitEvent(this, e.getMessage()));
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        Monopoly.debug("(" + id + ") Thread stopped");
    }

    public void sendCommand(Command output) {
        if (socket != null && !socket.isClosed()){
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(output);
                this.lastSendCommand = output;
                Monopoly.debug("Sending: " + output.getCmdType().name() + " " + String.join(" ", output.getArgs()));
            } catch (SocketException e){
                Server.getInstance().getEvents().executeEvent(new UserQuitEvent(this, e.getMessage()));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public static int getAmount(){return amount;}

    public void close() {
        try{
            if(socket != null && !socket.isClosed())
                socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
