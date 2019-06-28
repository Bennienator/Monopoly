package de.seben.monopoly.client;

import de.seben.monopoly.events.ConsoleCommandEvent;
import de.seben.monopoly.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientConsoleCommandHandler extends Thread{

    private static ClientConsoleCommandHandler instance;
    public static ClientConsoleCommandHandler getInstance(){
        if(instance == null)
            instance = new ClientConsoleCommandHandler();
        return instance;
    }

    private ClientConsoleCommandHandler(){}

    @Override
    public void run(){
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        while(true) {
            try {
                String in;
                if ((in = reader.readLine()) != null && !in.isEmpty()) {
                    Client.getInstance().getEvents().executeEvent(new ConsoleCommandEvent(in.split(" ")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
