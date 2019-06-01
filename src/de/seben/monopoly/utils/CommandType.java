package de.seben.monopoly.utils;

import java.io.Serializable;

public enum CommandType implements Serializable {

    END_OF_ROUND("EOR"),
    MOVE_PLAYER("MP"),
    MESSAGE("MSG");

    private String command;

    CommandType(String command){
        this.command = command;
    }

}
