package de.seben.monopoly.main;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.server.Server;

public class Monopoly {

    private static boolean client;
    private static boolean server;
    private static boolean debug;

    public static void main(String[] args){

        for(String arg : args){
            if(arg.equalsIgnoreCase("debug") && !debug){
                debug = true;
                debug("Debugging enabled");
            }else if(arg.equalsIgnoreCase("server") && !server){
                server = true;
                Server.getInstance().start();
            }else if(arg.equalsIgnoreCase("client") && !client){
                client = true;
                Client.getInstance().start();
            }
        }

    }

    public static void debug(String message){
        if(debug){
            System.out.println("[" + Thread.currentThread().getStackTrace()[2].getFileName().replaceAll(".java", "") + "] " + message);
        }
    }

}
