package de.seben.monopoly.server;

import de.seben.monopoly.events.UserQuitEvent;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClientController {

    private static ClientController instance;

    private HashMap<Integer, ClientConnection> clients = new HashMap<>();

    public synchronized static ClientController getInstance(){
        if(instance == null)
            instance = new ClientController();
        return instance;
    }
    private boolean running;

    private ClientController(){
        Monopoly.debug("Created instance");
    }

    public ClientController start(){
        if(!running){
            Monopoly.debug("Starting...");
            running = true;
            createNewClientConnection(Server.getInstance().getSocket());
        }
        return this;
    }

    public void createNewClientConnection(ServerSocket server){
        if(clients.size() < 4){
            for(int i = 0; i < 4; i++){
                if(!clients.containsKey(i)){
                    ClientConnection con = new ClientConnection(server, i);
                    clients.put(i, con);
                    con.start();
                    return;
                }
            }
        }
    }

    public void preRegisterPlayer(ClientConnection connection){
        int id = connection.getID();
        User created = Server.getInstance().getEngine().addUser(id);
        connection.setUser(created);
    }

    public void disconnect(ClientConnection connection){
        for(int i : clients.keySet()){
            if(clients.get(i).equals(connection)){
                engine.removeUser(i);
                clients.remove(i);
                createNewClientConnection(Server.getInstance().getSocket());
                return;
            }
        }
    }

    public void sendCommand(Command command, User... recipients){
        ArrayList<User> userList = new ArrayList<>(Arrays.asList(recipients));
        for (User user : userList) {
            clients.get(user.getID()).sendCommand(command);
        }
    }

    public void broadcastCommand(Command command){
        for(ClientConnection connection : clients.values()){
            connection.sendCommand(command);
        }
    }

    public ClientConnection getClientConnection(User user){
        return clients.get(user.getID());
    }

    public boolean isUsernameExisting(String username){
        for(ClientConnection connection : clients.values()){
            if(connection.getUser() != null && connection.getUser().getName() != null && connection.getUser().getName().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

}
