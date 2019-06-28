package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.events.UserQuitEvent;
import de.seben.monopoly.events.Event;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.server.User;

public class UserQuitListener implements EventListener {

    @Event
    public void onQuit(UserQuitEvent event){
        User user = event.getUser();
        String reason = event.getReason();
        String newReason = "";
        if(!reason.isEmpty()){
            if(reason.equalsIgnoreCase("Socket closed")){
                newReason = "Kicked";
            }else{
                newReason = reason;
            }
        }else{
            newReason = "";
        }
        Monopoly.debug(user.getName() + " (" + user.getID() + ") quit: " + newReason);
        Server.getInstance().getController().disconnect(event.getConnection());
    }

}