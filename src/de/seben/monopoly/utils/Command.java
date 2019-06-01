package de.seben.monopoly.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Command implements Serializable {

    private CommandType cmdType;
    private ArrayList<String> args;

    public Command(CommandType cmdType, String... args){

    }

}
