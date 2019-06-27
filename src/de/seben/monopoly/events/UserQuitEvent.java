package de.seben.monopoly.events;

import com.sun.org.apache.regexp.internal.recompile;

import de.seben.monopoly.server.ClientConnection;
import de.seben.monopoly.server.User;

public class UserQuitEvent implements IEvent{

    private ClientConnection connection;

    public UserQuitEvent(ClientConnection connection){
        this.connection = connection;
    }

    public User getUser(){
        return this.connection.getUser();
    }
    public ClientConnection getConnection(){
        return this.connection;
    }

}