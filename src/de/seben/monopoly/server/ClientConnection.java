package de.seben.monopoly.server;

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

    public ClientConnection(ServerSocket server){
        Monopoly.debug("Created instance");
        this.server = server;
    }

    public void run(){
        Monopoly.debug("Waiting");
        try {
            while (socket == null) {
                socket = server.accept();
                Monopoly.debug("Socket accepted");
                ClientController.getInstance().preRegisterPlayer(this);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        while(socket.isConnected()){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Command in = (Command) ois.readObject();
                Monopoly.debug("Command incomming");
                Server.getInstance().getController().analyseIncommingCommand(in, this);
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
        Server.getInstance().getEvents().executeEvent(new UserQuitEvent(user));
        Monopoly.debug("Disconnected");
        ClientController.getInstance().disconnect(this);
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
