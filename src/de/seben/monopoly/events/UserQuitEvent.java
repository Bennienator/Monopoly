package de.seben.monopoly.events;

import de.seben.monopoly.events.structure.IEvent;
import de.seben.monopoly.server.ClientConnection;
import de.seben.monopoly.utils.User;

public class UserQuitEvent implements IEvent {

    private ClientConnection connection;
    private String reason;

    public UserQuitEvent(ClientConnection connection, String reason){
        this.connection = connection;
        this.reason = reason;
    }

    public User getUser(){
        return this.connection.getUser();
    }
    public ClientConnection getConnection(){
        return this.connection;
    }
    public String getReason(){
        return this.reason;
    }

}