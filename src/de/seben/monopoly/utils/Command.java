package de.seben.monopoly.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Command implements Serializable {

    private static final long serialVersionUID = 129348938L;

    private CommandType cmdType;
    private ArrayList<String> args;

    public Command(CommandType cmdType, String... args){
        this.cmdType = cmdType;
        this.args = new ArrayList<>(Arrays.asList(args));
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(cmdType.getCommand());
        args.forEach(builder::append);
        return builder.toString();
    }

    public CommandType getCmdType(){ return cmdType; }

    public ArrayList<String> getArgs(){ return args; }

}
