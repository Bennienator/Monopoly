package de.seben.monopoly.server;

import java.net.ServerSocket;
import java.util.ArrayList;

public class ClientController {

    private ArrayList<ClientConnection> clients = new ArrayList<>();

    private static ClientController instance;
    public static ClientController getInstance(){
        if(instance == null)
            instance = new ClientController();
        return instance;
    }
    private ClientController(){}

    public void createNewClientConnection(ServerSocket server){
        if(clients.size() < 4){
            ClientConnection con = new ClientConnection(server);
            clients.add(con);
            con.start();
        }
    }

}
