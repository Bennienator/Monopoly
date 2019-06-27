package de.seben.monopoly.events;

import de.seben.monopoly.utils.Command;

public class ClientCommandRecieveEvent implements IEvent{

    private Command command;
    private String[] args;

    public ClientCommandRecieveEvent(Command command){
        this.command = command;
        this.args = command.getArgs();
    }

    public Command getCommand(){
        return this.command;
    }
    public String[] getArgs(){
        return this.args;
    }
    public String getArg(int index){
        return this.args[index];
    }

}