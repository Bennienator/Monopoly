package de.seben.monopoly.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import de.seben.monopoly.events.structure.EventManager;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.server.listeners.CommandReceiveListener;
import de.seben.monopoly.server.listeners.ConsoleCommandListener;
import de.seben.monopoly.server.listeners.UserLoginListener;
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
            System.out.println("Starting...");
            try {
                serverSocket = new ServerSocket(7777);
            }catch(Exception e){
                if(e instanceof BindException){
                    System.out.println("Port already in use");
                }else if(e instanceof IOException){
                    e.printStackTrace();
                }
                System.out.println("Exiting");
                System.exit(-1);
            }
            if(serverSocket.isBound()){
                Monopoly.debug("Started");
                ServerConsoleCommandHandler.getInstance().start();
                this.events = new EventManager();
                events.registerListener(new UserQuitListener());
                events.registerListener(new UserLoginListener());
                events.registerListener(new CommandReceiveListener());
                events.registerListener(new ConsoleCommandListener());
                this.engine = ServerEngine.getInstance();
                this.controller = ClientController.getInstance().start();
                System.out.println("Ready and waiting for players...");
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

	public void stop() {
        System.out.println("Shutting down...");
        System.exit(99);
	}
}
