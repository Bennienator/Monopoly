package de.seben.monopoly.server;

import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerEngine {

    private Plot[] plots;
    private HashMap<String, User> users;
    private ArrayList<String> keys;
    private int actUser;


    public ServerEngine(){
        plots = new Plot[41]; //0-39: Spielrunde (0: Start), 40 = Feld für Gefängnisinsassen (10: Gefängnisbesucher)
        //TODO: Alle Spielfelder erstellen
        users = new HashMap<>();
        keys = new ArrayList<>();
    }

    public void addUser(String name){
        users.put(name, new User(name));
        keys.add(name);
    }

    public void removeUser(String name){
        users.remove(name);
        keys.remove(name);
    }

    public void startRound(){
        actUser = (actUser + 1) % users.size();
        String keyAct = keys.get(actUser);
        ClientController.getInstance().broadcastCommand(new Command(CommandType.MESSAGE, users.get(keyAct).getName() + " ist nun an der Reihe."));
        int cubeOne = 0, cubeTwo = 0;
        for (int cubed = 1; cubeOne == cubeTwo; cubed++){
            cubeOne = (int) (Math.random() * 6) + 1;
            cubeTwo = (int) (Math.random() * 6) + 1;
            if (cubed == 3 && cubeOne == cubeTwo){
                intoPrison(actUser);
                ClientController.getInstance().broadcastCommand(new Command(CommandType.MESSAGE, "Weil " + users.get(keyAct).getName() + "3 Päsche hintereinander gewürfelt hat, muss er nun in das Gefängnis."));
                startRound();
                break;
            }
            int moves = cubeOne + cubeTwo;
            plots[users.get(keyAct).getPosition()].removeVisitor(users.get(keyAct));
            int newPos = users.get(keyAct).move(moves);
            plots[newPos].addVisitor(users.get(keyAct));
            ClientController.getInstance().broadcastCommand(new Command(CommandType.MOVE_PLAYER, keyAct, String.valueOf(newPos)));
            plots[newPos].activateEffect(users.get(keyAct));
        }

    }

    public void changeAmountHouses(int plotID, int amount){
        plots[plotID].changeAmountHouse(amount);
    }

    public void changeBalance(String userName, int amount){
        CommandType outputType = users.get(userName).changeBalance(amount) ? CommandType.ACCEPT : CommandType.REFUSE;
        if (amount < 0) ClientController.getInstance().sendCommand(new Command(outputType), userName);
    }

    public void intoPrison(int userPos){
        plots[users.get(userPos).getPosition()].removeVisitor(users.get(userPos));
        users.get(userPos).setPosition(40);
        plots[40].addVisitor(users.get(userPos));
        ClientController.getInstance().broadcastCommand(new Command(CommandType.MOVE_PLAYER, "40"));
    }

}
