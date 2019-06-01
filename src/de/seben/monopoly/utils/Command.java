package de.seben.monopoly.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Command implements Serializable {

    private CommandType cmdType;
    private ArrayList<String> args;

    public Command(CommandType cmdType, String... args){
        this.cmdType = cmdType;
        this.args = new ArrayList<>(Arrays.asList(args));
    }

    public String toString(){
        String command = cmdType.getCommand();
        for(String arg : args){
            command += " " + arg;
        }
        return command;
    }

    public CommandType getCmdType(){ return cmdType; }

}
