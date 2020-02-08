package de.seben.monopoly.client.listeners;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.events.ConsoleCommandEvent;
import de.seben.monopoly.events.Event;
import de.seben.monopoly.events.EventListener;
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
        }else if(command.equalsIgnoreCase("send")){
            try{
                if(args.size() == 0)
                    throw new IllegalArgumentException();
                CommandType commandType = CommandType.valueOf(args.get(0));
                String[] commandArgs = new String[args.size()-1];
                for(int i = 0; i < args.size()-1; i++){
                    commandArgs[i] = args.get(i+1);
                }
                Client.getInstance().getHandler().sendCommandToServer(new Command(commandType, commandArgs));
            }catch(IllegalArgumentException e){
                System.out.println("Command '" + command + "': Invalid parameters");
            }
        }else if(command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("leave") || command.equalsIgnoreCase("stop")){
            Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.DISCONNECT));
            System.exit(1);
        }else{
            System.out.println("Command '" + event.getCommand() + "' not found.");
        }
    }

}
