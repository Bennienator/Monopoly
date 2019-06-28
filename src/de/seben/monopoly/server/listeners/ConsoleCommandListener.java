package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.ConsoleCommandEvent;
import de.seben.monopoly.events.Event;
import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.server.User;

import java.util.ArrayList;

public class ConsoleCommandListener implements EventListener {

    @Event
    public void onConsoleCommand(ConsoleCommandEvent event) {
        if (event.getCommand().equalsIgnoreCase("kick")) {
            if (event.getArgs().size() > 0) {
                boolean success;
                User user;
                int id;
                String username;
                try {
                    id = Integer.valueOf(event.getArg(0));
                    username = "";
                    user = Server.getInstance().getEngine().getUserByID(id);
                    success = Server.getInstance().getEngine().kickUser(id);
                } catch (NumberFormatException e) {
                    id = -1;
                    username = String.join(" ", event.getArgs());
                    user = Server.getInstance().getEngine().getUserByUsername(username);
                    success = Server.getInstance().getEngine().kickUser(username);
                }
                if (success && user != null) {
                    System.out.println("You've kicked '" + user.getName() + "' (" + user.getID() + ")");
                } else {
                    System.out.println("User '" + (id == -1 ? username : id) + "' is not existing");
                }
            } else {
                System.out.println("Usage: /kick <Username/ID>");
            }
        }else if(event.getCommand().equalsIgnoreCase("info")){
            ArrayList<User> users = Server.getInstance().getEngine().getUsers();
            System.out.println("------ INFO ------");
            System.out.println("Player: " + users.size() + "/4");
            for(User user : users){
                System.out.println(" - " + user.getName());
            }
        }else if(event.getCommand().equalsIgnoreCase("help")) {
            System.out.println("------ HELP ------");
            System.out.println("/kick <Username/ID>");
            System.out.println("/info [Username/ID]");
        }else{
            System.out.println("This command is not valid");
        }
    }

}
