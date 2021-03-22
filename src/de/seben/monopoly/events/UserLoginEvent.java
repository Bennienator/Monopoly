package de.seben.monopoly.events;

import de.seben.monopoly.events.structure.IEvent;
import de.seben.monopoly.utils.User;

public class UserLoginEvent implements IEvent {

    private User user;

    public UserLoginEvent(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

}