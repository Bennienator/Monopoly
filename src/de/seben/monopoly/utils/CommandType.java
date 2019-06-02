package de.seben.monopoly.utils;

import java.io.Serializable;

public enum CommandType implements Serializable {

    END_OF_ROUND("EOR"),
    START_ROUND("SR"),
    MOVE_PLAYER("MP"),
    CLAIM_PLOT("CP"),
    BUILD_HOUSE("BH"),
    REMOVE_HOUSE("RH"),
    BUY_PLOT("BP"),
    PAY("PAY"),
    EARN("EARN"),
    SET_MONEY("SM"),
    MESSAGE("MSG"),
    DISCONNECT("DC"),
    ACCEPT("ACPT"),
    CHAT("CHAT");

    private String command;

    CommandType(String command){
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
}
