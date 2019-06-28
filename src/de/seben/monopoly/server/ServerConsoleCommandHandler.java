package de.seben.monopoly.server;

import de.seben.monopoly.events.ConsoleCommandEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConsoleCommandHandler extends Thread{

    private static ServerConsoleCommandHandler instance;
    public static ServerConsoleCommandHandler getInstance(){
        if(instance == null)
            instance = new ServerConsoleCommandHandler();
        return instance;
    }

    private ServerConsoleCommandHandler(){}

    @Override
    public void run(){
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        while(true) {
            try {
                String in;
                if ((in = reader.readLine()) != null && !in.isEmpty()) {
                    Server.getInstance().getEvents().executeEvent(new ConsoleCommandEvent(in.split(" ")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
