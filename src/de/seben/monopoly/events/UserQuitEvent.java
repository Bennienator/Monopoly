package de.seben.monopoly.events;

import de.seben.monopoly.server.User;

public class UserQuitEvent implements IEvent{

    private User user;

    public UserQuitEvent(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

}