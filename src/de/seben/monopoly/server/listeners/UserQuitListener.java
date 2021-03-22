package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.structure.EventListener;
import de.seben.monopoly.events.UserQuitEvent;
import de.seben.monopoly.events.structure.Event;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.utils.User;

public class UserQuitListener implements EventListener {

    @Event
    public void onQuit(UserQuitEvent event){
        User user = event.getUser();
        String reason = event.getReason();
        System.out.println(user.getName() + " (" + user.getID() + ") quit: " + reason);
        Server.getInstance().getController().disconnect(event.getConnection());
        Server.getInstance().getController().broadcastUserList();
    }

}