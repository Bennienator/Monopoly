package de.seben.monopoly.client;

import de.seben.monopoly.client.listeners.CommandReceiveListener;
import de.seben.monopoly.client.listeners.ConsoleCommandListener;
import de.seben.monopoly.events.EventManager;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static Client instance;
    public static Client getInstance(){
        if(instance == null)
            instance = new Client();
        return instance;
    }

    private Socket socket;
    private SpielfeldFrame frame;
    private CommandHandler handler;
    private EventManager events;

    private String hostname = "localhost";
    private int port = 7777;

    private String username;

    private boolean running;

    private Client(){
        Monopoly.debug("Created instance");
    }

    public void start(){
        if(!running){
            running = true;
            Monopoly.debug("Starting...");
            ClientConsoleCommandHandler.getInstance().start();
            events = new EventManager();
            events.registerListener(new CommandReceiveListener());
            events.registerListener(new ConsoleCommandListener());
            handler = new CommandHandler();
            connect();
        }
    }

    public void connect(){
        try {
            socket = new Socket(hostname, port);
            Monopoly.debug("Connected to Server");
            handler.start();
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                Monopoly.debug("Unknown Host");
            } else if (e instanceof ConnectException) {
                Monopoly.debug("Port closed");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void addChatMessage(String message){
        frame.addChatMessage(message);
    }

    public void movePlayer(String name, int plotID){

    }

    public void setOwner(String name, int plotID){

    }

    public void changeAmountHouses(int plotID, int amount){

    }

    public void changeCreditPlayer(String name, int value){

    }

    public void setCreditPlayer(String name, int value){

    }

    public void startRound(){

    }

    public Socket getSocket(){ return socket; }
    public CommandHandler getHandler(){
        return this.handler;
    }
    public EventManager getEvents(){
        return this.events;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    public void disconnect(){
        if(socket.isConnected()){
            try {
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void sendDisconnect(){
        if(socket.isConnected()){
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(new Command(CommandType.DISCONNECT));
                Monopoly.debug("Send Command: " + CommandType.DISCONNECT.name());
                oos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            try{
                socket.close();
                Monopoly.debug("Connection closed");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void resetConnection(){
        if(socket.isClosed()){
            handler = new CommandHandler();
            connect();
        }
    }

}
