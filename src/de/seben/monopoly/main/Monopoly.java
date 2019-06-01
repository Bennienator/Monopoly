package de.seben.monopoly.main;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.server.Server;

public class Monopoly {

    public static void main(String[] args){

        System.out.println("Test");

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("server")){
                new Server();
            }else if(args[0].equalsIgnoreCase("client")){
                new Client();
            }
        }


    }

}
