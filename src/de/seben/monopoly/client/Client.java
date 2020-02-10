package de.seben.monopoly.client;

import de.seben.monopoly.client.frames.ChatFrame;
import de.seben.monopoly.client.frames.ConnectFrame;
import de.seben.monopoly.client.frames.PitchFrame;
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
    private PitchFrame pitchFrame;
    private ChatFrame chatFrame;
    private ConnectFrame connectFrame;
    private CommandHandler handler;
    private EventManager events;
    private PlayerController players;

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
            players = PlayerController.getInstance();
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
                System.exit(400);
            } else if (e instanceof ConnectException) {
                Monopoly.debug("Port " + port + " on host computer is closed");
                System.exit(401);
            } else {
                e.printStackTrace();
                System.exit(402);
            }
        }
    }

    public void addChatMessage(String sender, String message){
        chatFrame.addChatMessage(sender, message);
    }
    public void addPrivateChatMessage(String sender, String message) {
        chatFrame.addPrivateChatMessage(sender, message);
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
    public PlayerController getPlayers() {
        return this.players;
    }

    public ConnectFrame getConnectFrame() {
        return this.connectFrame;
    }
    public ChatFrame getChatFrame() {
        return this.chatFrame;
    }
    public PitchFrame getPitchFrame() {
        return this.pitchFrame;
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

    public void loggedIn() {
        connectFrame = new ConnectFrame();
        chatFrame = new ChatFrame();
    }
}
