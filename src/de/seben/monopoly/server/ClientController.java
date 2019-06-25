package de.seben.monopoly.server;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClientController {

    private static ClientController instance;

    private HashMap<Integer, ClientConnection> clients = new HashMap<>();
    private ServerEngine engine;

    public static ClientController getInstance(){
        if(instance == null)
            instance = new ClientController();
        return instance;
    }
    private boolean running;

    private ClientController(){
        engine = new ServerEngine();
    }

    public ClientController start(){
        if(!running){
            running = true;
            for(int i = 0; i < 4; i++){
                createNewClientConnection(Server.getInstance().getSocket());
            }
        }
        return this;
    }

    public void createNewClientConnection(ServerSocket server){
        if(clients.size() < 4){
            for(int i = 0; i < 4; i++){
                if(!clients.containsKey(i)){
                    ClientConnection con = new ClientConnection(server);
                    clients.put(i, con);
                    con.start();
                    return;
                }
            }
        }
    }

    public void preRegisterPlayer(ClientConnection connection){
        connection.setUser(engine.addUser(connection.getID()));
    }

    public void disconnect(ClientConnection connection){
        for(int i : clients.keySet()){
            if(clients.get(i).equals(connection)){
                engine.removeUser(userID);
                clients.remove(i);
                createNewClientConnection(Server.getInstance().getSocket()); //Prepare socket for next connection
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

    public void analyseIncommingCommand(Command command){
        CommandType cmdType = command.getCmdType();
        ArrayList<String> args = command.getArgs();
        switch (cmdType){
            case END_OF_ROUND: // args: null
                engine.nextRound();
                break;
            case CLAIM_PLOT: // args: PlayerID, plotID
                int userID = Integer.valueOf(args.get(0));
                int plotID = Integer.valueOf(args.get(1));
                break;
            case BUILD_HOUSE: // args: plotID
                engine.changeAmountHouses(Integer.valueOf(args.get(1)), 1);
                break;
            case REMOVE_HOUSE: // args: plotID
                engine.changeAmountHouses(Integer.valueOf(args.get(0)), -1);
                break;
            case PAY: // args: PlayerID, Höhe des Betrages, Name des Zielspielers
                engine.changeBalance(Integer.valueOf(args.get(0)), -1 * Integer.valueOf(args.get(1)));
                engine.changeBalance(Integer.valueOf(args.get(2)), Integer.valueOf(args.get(1)));
                break;
            case EARN: // args: PlayerID, Höhe des Betrages
                engine.changeBalance(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
                break;
            case CHAT: // args: PlayerID, Nachricht
                int id = Integer.valueOf(args.get(0));
                String message = String.join(" ", args.subList(1, args.size() - 1));
                break;
        }
    }

}
