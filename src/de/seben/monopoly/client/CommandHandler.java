package de.seben.monopoly.client;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

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
            while (true) {
                Command input = (Command) ois.readObject();
                CommandType cmdType = input.getCmdType();
                switch (cmdType){
                    case CHAT:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
