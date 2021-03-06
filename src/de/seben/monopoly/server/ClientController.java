package de.seben.monopoly.server;

import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;
import de.seben.monopoly.utils.User;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            for(int i = 0; i < 4; i++) {
                createNewClientConnection(Server.getInstance().getSocket());
            }
        }
        return this;
    }

    public void createNewClientConnection(ServerSocket server){
        if(clients.size() < 4){
            ClientConnection con = new ClientConnection(server);
            clients.put(con.getID(), con);
            con.start();
        }
    }

    public void preRegisterPlayer(ClientConnection connection){
        connection.setUser(Server.getInstance().getEngine().addUser(connection.getID()));
    }

    public void disconnect(ClientConnection connection){
        if(connection != null) {
            connection.close();
            Server.getInstance().getEngine().removeUser(connection.getID());
            clients.remove(connection.getID());
            createNewClientConnection(Server.getInstance().getSocket());
        }
    }
    public void disconnect(int id){
        this.disconnect(clients.get(id));
    }
    public void disconnect(User user){
        this.disconnect(user.getID());
    }

    public void sendCommand(Command command, User... recipients){
        for (User user : recipients) {
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
    public ClientConnection getClientConnection(int id){
        return clients.get(id);
    }

    public boolean isUsernameExisting(String username){
        for(ClientConnection connection : clients.values()){
            if(connection.getUser() != null && connection.getUser().getName() != null && connection.getUser().getName().equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

    public void broadcastUserList() {
        ArrayList<User> users = Server.getInstance().getEngine().getUsers();
        String[] userStr = new String[users.size()*3];
        int i = 0;
        for(User user : users){
            userStr[i] = String.valueOf(user.getID());
            i++;
            userStr[i] = user.getName();
            i++;
            userStr[i] = String.valueOf(user.isReady());
            i++;
        }
        broadcastCommand(new Command(CommandType.PLAYERLIST, userStr));
    }
}
