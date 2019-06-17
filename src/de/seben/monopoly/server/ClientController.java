package de.seben.monopoly.server;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClientController {

    private static ClientController instance;

    private HashMap<String, ClientConnection> clients = new HashMap<>();
    private ArrayList<String> nameUsers;
    private ServerEngine engine;

    public static ClientController getInstance(){
        if(instance == null)
            instance = new ClientController();
        return instance;
    }

    private ClientController(){
        engine = new ServerEngine();
    }

    public void createNewClientConnection(ServerSocket server){
        if(clients.size() < 4){
            ClientConnection con = new ClientConnection(server);
            clients.put("", con);
            con.start();
        }
    }

    public void sendCommand(Command output, String... recipients){
        ArrayList<String> nameList = new ArrayList<>(Arrays.asList(recipients));
        for (int i = 0; i < nameList.size(); i++) {
            ClientConnection recipientConnection = clients.get(nameList.get(i));
            recipientConnection.sendCommand(output);
        }
    }

    public void broadcastCommand(Command output){
        for (int i = 0; i < clients.size(); i++){
            clients.get(i).sendCommand(output);
        }
    }

    public void analyseCommand(Command input){
        CommandType cmdType = input.getCmdType();
        ArrayList<String> args = input.getArgs();
        switch (cmdType){
            case END_OF_ROUND: // args: null
                engine.startRound();
                break;
            case CLAIM_PLOT: // args: Name des Spielers, plotID
                break;
            case BUILD_HOUSE: // args: Name des Spielers, plotID
                engine.changeAmountHouses(nameUsers.indexOf(args.get(0)), Integer.valueOf(args.get(1)), 1);
                break;
            case REMOVE_HOUSE: // args: Name des Spielers, plotID
                engine.changeAmountHouses(nameUsers.indexOf(args.get(0)), Integer.valueOf(args.get(0)), -1);
                break;
            case PAY: // args: Name des Spielers, Höhe des Betrages, Ziel
                break;
            case EARN: // args: Name des Spielers, Höhe des Betrages
            case DISCONNECT: // args: Name des Spielers
                break;
            case CHAT: // args: Name des Spielers, Nachricht
                break;
        }
    }

}
