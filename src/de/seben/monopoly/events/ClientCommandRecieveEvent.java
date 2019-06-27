package de.seben.monopoly.events;

import java.util.ArrayList;

import de.seben.monopoly.utils.Command;

public class ClientCommandRecieveEvent implements IEvent{

    private Command command;
    private Command lastCommand;
    private ArrayList<String> args;

    public ClientCommandRecieveEvent(Command command, Command lastCommand){
        this.command = command;
        this.lastCommand = lastCommand;
        this.args = command.getArgs();
    }

    public Command getCommand(){
        return this.command;
    }
    public Command getLastCommand(){
        return this.lastCommand;
    }
    public ArrayList<String> getArgs(){
        return this.args;
    }
    public String getArg(int index){
        return this.args[index];
    }

}