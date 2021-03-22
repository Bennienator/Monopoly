package de.seben.monopoly.events;

import java.util.ArrayList;

import de.seben.monopoly.events.structure.IEvent;
import de.seben.monopoly.utils.User;
import de.seben.monopoly.utils.Command;

public class ServerCommandReceiveEvent implements IEvent {

    private User commandSender;
    private Command command;
    private ArrayList<String> args;

    public ServerCommandReceiveEvent(User commandServer, Command command){
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
    public ArrayList<String> getArgs(){
        return this.args;
    }
    public String getArg(int index){
        return this.args.get(index);
    }

}