package de.seben.monopoly.client;

import de.seben.monopoly.client.listeners.CommandRecieveListener;
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

    private boolean running;

    private Client(){
        Monopoly.debug("Created instance");
    }

    public void start(){
        if(!running){
            Monopoly.debug("Starting...");
            running = true;
            while(socket == null) {
                try {
                    socket = new Socket("localhost", 7777);
                    Monopoly.debug("Socket connected");
                    events = new EventManager();
                    events.registerListener(new CommandRecieveListener());
                    handler = new CommandHandler(this);
                    handler.start();
                } catch (Exception e) {
                    if(e instanceof UnknownHostException){
                        Monopoly.debug("Unknown Host");
                    }else if(e instanceof ConnectException){
                        Monopoly.debug("Port closed");
                    }else if(e instanceof IOException){
                        e.printStackTrace();
                    }
                    try {
                        Monopoly.debug("Trying again (10s)");
                        Thread.sleep(10000);
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
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

    public void sendMessageToServer(Command outCommand){
        if(socket.isConnected()){
            try{
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(outCommand);
                Monopoly.debug("Send command: " + outCommand.getCmdType().name() + " " + String.join(" ", outCommand.getArgs()));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void disconnect(){
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

}
