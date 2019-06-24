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
    private ArrayList<String> keys = new ArrayList<>();
    private ServerEngine engine;
    private int amountConnections = 0;

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
            clients.put("Slot" + amountConnections, con);
            keys.add("Slot" + amountConnections);
            amountConnections++;
            con.start();
        }
    }

    public void connect(String nameUser, ClientConnection con){
        clients.remove(con);
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
            if (!keys.get(i).equals("")) clients.get(keys.get(i)).sendCommand(output);
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
            case BUILD_HOUSE: // args: plotID
                engine.changeAmountHouses(Integer.valueOf(args.get(1)), 1);
                break;
            case REMOVE_HOUSE: // args: plotID
                engine.changeAmountHouses(Integer.valueOf(args.get(0)), -1);
                break;
            case PAY: // args: Name des Spielers, Höhe des Betrages, Name des Zielspielers
                engine.changeBalance(args.get(0), -1 * Integer.valueOf(args.get(1)));
                engine.changeBalance(args.get(2), Integer.valueOf(args.get(1)));
                break;
            case EARN: // args: Name des Spielers, Höhe des Betrages
                engine.changeBalance(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case DISCONNECT: // args: Name des Spielers
                String nameUser = args.get(0);
                engine.removeUser(nameUser);
                ClientConnection con = clients.remove(nameUser);
                keys.remove(nameUser);
                clients.put("", con);
                keys.add("");
                con.start();
                break;
            case CHAT: // args: Name des Spielers, Nachricht
                String username = args.get(0);
                String message = String.join(" ", args.subList(1, args.size() - 1));
                break;
        }
    }

}
