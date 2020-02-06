package de.seben.monopoly.client;

import de.seben.monopoly.events.ClientCommandRecieveEvent;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class CommandHandler extends Thread{

    private Command lastSendCommand;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public CommandHandler(){
        Monopoly.debug("Created instance");
    }

    public void run(){ // Commands vom Server werden bearbeitet
        Monopoly.debug("Ready...");
        String username = "";
        try{
            username = JOptionPane.showInputDialog(null, "Bitte gebe deinen Benutzernamen ein.", "Monopoly - Login", JOptionPane.QUESTION_MESSAGE);
        }catch(Exception e){
            if(username == null || username.isEmpty()){
                username = "guest" + (int) (Math.random()*100);
            }
        }
        
        Monopoly.debug("Logging in as '" + username + "'");
        sendCommandToServer(new Command(CommandType.LOGIN, username));
        try {
            while (Client.getInstance().getSocket() != null && !Client.getInstance().getSocket().isClosed()) {
                ois = new ObjectInputStream(Client.getInstance().getSocket().getInputStream());
                Object in;
                if ((in = ois.readObject()) != null) {
                    Command input = (Command) in;
                    Client.getInstance().getEvents().executeEvent(new ClientCommandRecieveEvent(input, lastSendCommand));
                }
            }
        }catch (EOFException e){
            try {
                sleep(10);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        } catch(SocketException e){
            Monopoly.debug("Lost connection to Server: " + e.getMessage());
            Client.getInstance().disconnect();
        } catch (IOException | ClassNotFoundException | NullPointerException e){
            e.printStackTrace();
        }
        Monopoly.debug("Connection lost");
        System.exit(404);
    }

    public void sendCommandToServer(Command output){
        if (Client.getInstance().getSocket() != null && !Client.getInstance().getSocket().isClosed()) {
            try {
                oos = new ObjectOutputStream(Client.getInstance().getSocket().getOutputStream());
                oos.writeObject(output);
                this.lastSendCommand = output;
                Monopoly.debug("Sending: " + output.getCmdType().name() + " " + String.join(" ", output.getArgs()));
            } catch (SocketException e){
                Monopoly.debug("Lost connection to Server: " + e.getMessage());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
