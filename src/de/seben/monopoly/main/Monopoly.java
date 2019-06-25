package de.seben.monopoly.main;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Monopoly {

    public static void main(String[] args){

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("server")){
                Server.getInstance().start();
            }else if(args[0].equalsIgnoreCase("client")){
                new Client();
            }
        }
    }

}
