package de.seben.monopoly.utils;

import java.io.Serializable;

public enum CommandType implements Serializable {

    //GAME
    END_OF_ROUND("EOR"),
    START_ROUND("SR"),

    //PLAYER
    MOVE_PLAYER("MP"),
    CLAIM_PLOT("CP"),
    BUILD_HOUSE("BH"),
    REMOVE_HOUSE("RH"),
    BUY_PLOT("BP"),
    PAY("PAY"),
    EARN("EARN"),
    SET_MONEY("SM"),

    //COMMUNICATION
    MESSAGE("MSG"),
    CHAT("CHAT"),
    INFO("INFO"),

    //SYSTEM
    CONNECT("CC"),
    ACCEPT("ACPT"),
    DISCONNECT("DC"),
    REFUSE("RFS"),
    LOGIN("LOGIN"),
    READY("RDY"),
    KICK("KICK");

    private String command;

    CommandType(String command){
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
}
