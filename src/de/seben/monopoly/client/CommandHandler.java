package de.seben.monopoly.client;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class CommandHandler extends Thread{

    private Client owner;
    private ObjectInputStream ois;

    public CommandHandler(Client client){
        try {
            this.owner = owner;
            ois = (ObjectInputStream) owner.getSocket().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){ // Commands vom Server werden bearbeitet
        try {
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
