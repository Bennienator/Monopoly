package de.seben.monopoly.client;

import de.seben.monopoly.client.listeners.CommandRecieveListener;
import de.seben.monopoly.events.ClientCommandRecieveEvent;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CommandHandler extends Thread{

    private Client client;
    private Command lastSendCommand;

    public CommandHandler(){
        Monopoly.debug("Created instance");
        this.client = Client.getInstance();
    }

    public void run(){ // Commands vom Server werden bearbeitet
        Monopoly.debug("Waiting...");
        //String username = JOptionPane.showInputDialog(null, "Bitte gebe deinen Benutzernamen ein.", "Monopoly - Login", JOptionPane.QUESTION_MESSAGE);
        String username = "Bennienator";
        Monopoly.debug("Logging in as '" + username + "'");
        sendCommandToServer(new Command(CommandType.LOGIN, username));
        while (client.getSocket().isConnected()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
                Object in;
                if ((in = ois.readObject()) != null) {
                    Command input = (Command) in;
                    Client.getInstance().getEvents().executeEvent(new ClientCommandRecieveEvent(input, lastSendCommand));
                }
            }catch (EOFException e){
                try {
                    sleep(10);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        Monopoly.debug("Disconnected");
    }

    public void sendCommandToServer(Command output){
        try {
            if (client.getSocket() != null && client.getSocket().isConnected()) {
                ObjectOutputStream oos = new ObjectOutputStream(client.getSocket().getOutputStream());
                oos.writeObject(output);
                this.lastSendCommand = output;
                Monopoly.debug("Sending: " + output.getCmdType().name() + " " + String.join(" ", output.getArgs()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
