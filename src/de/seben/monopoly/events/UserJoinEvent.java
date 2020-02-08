package de.seben.monopoly.events;

import de.seben.monopoly.utils.User;

public class UserJoinEvent implements IEvent{

    private User user;

    public UserJoinEvent(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

}