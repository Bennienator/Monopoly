package de.seben.monopoly.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import de.seben.monopoly.events.EventManager;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.server.listeners.CommandRecieveListener;
import de.seben.monopoly.server.listeners.UserQuitListener;

public class Server {

    private static Server instance;
    public static Server getInstance(){
        if(instance == null)
            instance = new Server();
        return instance;
    }

    private boolean running;

    private ServerSocket serverSocket;
    private ClientController controller;
    private ServerEngine engine;
    private EventManager events;

    private Server(){
        Monopoly.debug("Created instance");
    }

    public void start(){
        if(!running){
            running = true;
            Monopoly.debug("Starting...");
            try {
                serverSocket = new ServerSocket(7777);
            }catch(Exception e){
                if(e instanceof BindException){
                    Monopoly.debug("Port already in use");
                }else if(e instanceof IOException){
                    e.printStackTrace();
                }
                Monopoly.debug("Exiting");
                System.exit(-1);
            }
            if(serverSocket.isBound()){
                Monopoly.debug("Started");
                this.events = new EventManager();
                events.registerListener(new UserQuitListener());
                events.registerListener(new CommandRecieveListener());
                this.engine = ServerEngine.getInstance();
                this.controller = ClientController.getInstance().start();
            }
        }
    }

    public ServerSocket getSocket(){
        return this.serverSocket;
    }

    public ClientController getController() {
        return controller;
    }

    public ServerEngine getEngine() {
        return engine;
    }

    public EventManager getEvents() {
        return events;
    }
}
