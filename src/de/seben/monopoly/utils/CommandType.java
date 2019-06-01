package de.seben.monopoly.utils;

import java.io.Serializable;

public enum CommandType implements Serializable {

    END_OF_ROUND("EOR"),
    MOVE_PLAYER("MP"),
    CLAIM_PLOT("CP"),
    BUILD_HOUSE("BH"),
    PAY("PAY"),
    EARN("EARN"),
    SET_MONEY("SM"),
    MESSAGE("MSG"),
    CHAT("CHAT");

    private String command;

    CommandType(String command){
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
}
