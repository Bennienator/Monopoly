package de.seben.monopoly.client;

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

    public void disconnect(){
        if(socket != null){
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(new Command(CommandType.DISCONNECT));
                oos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            try{
                socket.close();
                Monopoly.debug("Connection lost");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
