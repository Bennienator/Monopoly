package de.seben.monopoly.events;

public class UserQuitEvent implements IEvent{

    private User user;

    public UserQuitEvent(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

}