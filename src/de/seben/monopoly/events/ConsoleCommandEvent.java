package de.seben.monopoly.events;

import de.seben.monopoly.events.structure.IEvent;

import java.util.ArrayList;

public class ConsoleCommandEvent implements IEvent {

    private String command;
    private ArrayList<String> args = new ArrayList<>();

    public ConsoleCommandEvent(String[] message){
        this.command = message[0];
        for(int i = 1; i < message.length; i++){
            args.add(message[i]);
        }
    }

    public String getCommand(){
        return this.command;
    }
    public ArrayList<String> getArgs(){
        return this.args;
    }
    public String getArg(int index) {
        if (args.size() > index) {
            return this.args.get(index);
        }else{
            return "null";
        }
    }

}
