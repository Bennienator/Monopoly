package de.seben.monopoly.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket socket;
    private SpielfeldFrame frame;
    private CommandHandler contanct;

    public Client(){

        System.out.println("Servus! Ich bin ein Client.");

        try {
            socket = new Socket("localhost", 7777);
            System.out.println("Verbindung hergestellt!");
        }catch (UnknownHostException e){
            System.out.println("Dieser Host ist nicht bekannt!");
        }catch (ConnectException e){
            System.out.println("Der Port ist nicht geöffnet!");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(socket != null){
                try{
                    socket.close();
                    System.out.println("Verbindung zum Server geschlossen!");
                    contanct = new CommandHandler(this);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public Socket getSocket(){ return socket; }

}
