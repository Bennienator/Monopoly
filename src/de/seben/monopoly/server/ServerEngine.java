package de.seben.monopoly.server;

import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.util.ArrayList;

public class ServerEngine {

    private Plot[] plots;
    private ArrayList<User> users;
    private int actUser;


    public ServerEngine(){
        plots = new Plot[41]; //0-39: Spielrunde (0: Start), 40 = Feld für Gefängnisinsassen (10: Gefängnisbesucher)
        //TODO: Alle Spielfelder erstellen
        users = new ArrayList<>(4);
    }

    public void startRound(){
        do {
            actUser = (actUser + 1) % 4;
        } while (users.get(actUser) == null);
        ClientController.getInstance().broadcastCommand(new Command(CommandType.MESSAGE, users.get(actUser).getName() + " ist nun an der Reihe."));
        int cubeOne = 0, cubeTwo = 0;
        for (int cubed = 1; cubeOne == cubeTwo; cubed++){
            cubeOne = (int) (Math.random() * 6) + 1;
            cubeTwo = (int) (Math.random() * 6) + 1;
            if (cubed == 3 && cubeOne == cubeTwo){
                intoPrison(actUser);
                ClientController.getInstance().broadcastCommand(new Command(CommandType.MESSAGE, "Weil " + users.get(actUser).getName() + "3 Päsche hintereinander gewürfelt hat, muss er nun in das Gefängnis."));
                startRound();
                break;
            }
            int moves = cubeOne + cubeTwo;
            plots[users.get(actUser).getPosition()].removeVisitor(users.get(actUser));
            int newPos = users.get(actUser).move(moves);
            plots[newPos].addVisitor(users.get(actUser));
            ClientController.getInstance().broadcastCommand(new Command(CommandType.MOVE_PLAYER, String.valueOf(newPos)));
            plots[newPos].activateEffect();
        }
    }

    public void changeAmountHouses(int userID, int plotID, int amount){
        User plotOwner = plots[plotID].getOwner();
        if (plotOwner != null){
            if (userID != users.indexOf(plots[plotID].getOwner())){
                plots[plotID].changeAmountHouse(amount);
                return;
            }
        }
        ClientController.getInstance().sendCommand(new Command(CommandType.MESSAGE, "Du kannst die Häuser von diesem Grundstück nicht verwalten, weil du nicht der Eigentümer von diesem Grundstück bist."), users.get(userID).getName());
    }

    public void intoPrison(int userPos){
        plots[users.get(userPos).getPosition()].removeVisitor(users.get(userPos));
        users.get(userPos).setPosition(40);
        plots[40].addVisitor(users.get(userPos));
        ClientController.getInstance().broadcastCommand(new Command(CommandType.MOVE_PLAYER, "40"));
    }

}
