package de.seben.monopoly.server;

import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ServerEngine {

    private Plot[] plots;
    private HashMap<Integer, User> users;
    private int actUser;


    public ServerEngine(){
        plots = new Plot[41]; //0-39: Spielrunde (0: Start), 40 = Feld für Gefängnisinsassen (10: Gefängnisbesucher)
        //TODO: Alle Spielfelder erstellen
        users = new HashMap<>();
    }

    public User addUser(int id){ //Preregistration
        User user = new User(id);
        users.put(id, user);
        return user;
    }
    public User addUser(int id, String name){
        User user = new User(id, name);
        users.put(id, user);
        return user;
    }

    public void removeUser(int userID){
        users.remove(userID);
    }

    public void nextPlayer(){
        actUser = (actUser + 1) % users.size();
        User user = users.get(actUser);
        ClientController.getInstance().broadcastCommand(new Command(CommandType.MESSAGE, user.getName() + " ist nun an der Reihe."));
        int cubeOne = 0, cubeTwo = 0;
        for (int cubed = 1; cubeOne == cubeTwo; cubed++){
            Random random = new Random();
            cubeOne = 1 + random.nextInt(6);
            cubeTwo = 1 + random.nextInt(6);
            if (cubed == 3 && cubeOne == cubeTwo){
                intoPrison(actUser);
                ClientController.getInstance().broadcastCommand(new Command(CommandType.MESSAGE, "Weil " + user.getName() + " 3 Päsche hintereinander gewürfelt hat, muss er nun in das Gefängnis."));
                nextPlayer();
                return;
            }
            int moves = cubeOne + cubeTwo;
            plots[user.getPosition()].removeVisitor(user);
            int newPos = user.move(moves);
            plots[newPos].addVisitor(user);
            ClientController.getInstance().broadcastCommand(new Command(CommandType.MOVE_PLAYER, actUser, newPos));
            plots[newPos].activateEffect(user);
        }

    }

    public void changeAmountHouses(int plotID, int amount){
        plots[plotID].changeAmountHouse(amount);
    }

    public void changeBalance(int userID, int amount){
        User user = users.get(userID);
        CommandType outputType = user.changeBalance(amount) ? CommandType.ACCEPT : CommandType.REFUSE;
        ClientController.getInstance().sendCommand(new Command(outputType), user);
    }

    public void intoPrison(int userID){
        User user = users.get(userID);
        plots[user.getPosition()].removeVisitor(user);
        user.setPosition(40);
        plots[40].addVisitor(user);
        ClientController.getInstance().broadcastCommand(new Command(CommandType.MOVE_PLAYER, userID, 40));
    }

}
