package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.events.UserQuitEvent;
import de.seben.monopoly.events.Event;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.server.User;

public class UserQuitListener implements EventListener{

    @Event
    public void onQuit(UserQuitEvent event){
        User user = event.getUser();
        Monopoly.debug(user.getName() + " (" + user.getID() + ") left");
    }

}