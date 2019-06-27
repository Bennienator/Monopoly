package de.seben.monopoly.events;

import de.seben.monopoly.server.User;
import de.seben.monopoly.utils.Command;

public class ServerCommandRecieveEvent implements IEvent{

    private User commandSender;
    private Command command;
    private String[] args;

    public ServerCommandRecieveEvent(User commandServer, Command command){
        this.commandSender = commandServer;
        this.command = command;
        this.args = command.getArgs();
    }

    public User getCommandSender(){
        return this.commandSender;
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