package de.seben.monopoly.client.listeners;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.events.ConsoleCommandEvent;
import de.seben.monopoly.events.Event;
import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.util.ArrayList;

public class ConsoleCommandListener implements EventListener {

    @Event
    public void onConsoleCommand(ConsoleCommandEvent event){
        String command = event.getCommand();
        ArrayList<String> args = event.getArgs();
        if(command.equalsIgnoreCase("help")){
            System.out.println("------ HILFE ------");
            System.out.println("/info [Username/ID]");
        }else if(command.equalsIgnoreCase("info")){
            Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.INFO));
        }else if(command.equalsIgnoreCase("send")){
            try{
                CommandType commandType = CommandType.valueOf(args.get(0));
                String[] commandArgs = new String[args.size()-1];
                if(commandType != null){
                    for(int i = 0; i < args.size()-1; i++){
                        commandArgs[i] = args.get(i+1);
                    }
                    Client.getInstance().getHandler().sendCommandToServer(new Command(commandType, commandArgs));
                    System.out.println("Sending: " + commandType.name() + " " + String.join(" ", commandArgs));
                }
            }catch(IllegalArgumentException e){
                System.out.println("This is not a kind of message: " + args.get(0));
            }
        }
    }

}
