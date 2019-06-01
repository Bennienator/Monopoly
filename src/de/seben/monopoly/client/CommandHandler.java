package de.seben.monopoly.client;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class CommandHandler extends Thread{

    private Client owner;

    public CommandHandler(Client client){
        this.owner = client;
    }

    public void run(){ // Commands vom Server werden bearbeitet
        try {
            ObjectInputStream ois = new ObjectInputStream(owner.getSocket().getInputStream());
            Object in;
            while (true) {
                try {
                    if ((in = ois.readObject()) != null) {
                        Command input = (Command) in;
                        CommandType cmdType = input.getCmdType();
                        switch (cmdType) {
                            case ACCEPT:
                                System.out.println("Client: Accept");
                                break;
                            case CHAT:
                                StringBuilder message = new StringBuilder();
                                String[] parts = input.toString().split(" ");
                                for (int i = 1; i < parts.length; i++) {
                                    message.append(parts[i]);
                                }
                                owner.addChatMessage(message.toString());
                                break;

                        }
                    }
                }catch (EOFException e){

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
