package de.seben.monopoly.server;

import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.util.HashMap;
import java.util.Random;

public class ServerEngine {

    private static ServerEngine instance;
    public static ServerEngine getInstance(){
        if(instance == null)
            instance = new ServerEngine();
        return instance;
    }

    private Plot[] plots;
    private HashMap<Integer, User> users = new HashMap<>();
    private int actUser;

    private ServerEngine(){
        Monopoly.debug("Created instance");
        init();
    }

    private void init(){
        Monopoly.debug("Initializing...");
        plots = new Plot[41]; //0-39: Spielrunde (0: Start), 40 = Feld für Gefängnisinsassen (10: Gefängnisbesucher)
        //TODO: Alle Spielfelder erstellen
    }

    public User addUser(int id){ //Preregistration
        User user = new User(id);
        users.put(id, user);
        return user;
    }
    public User addUser(int id, String name){
        User user = this.addUser(id);
        user.setName(name);
        return user;
    }

    public void removeUser(int userID){
        users.remove(userID);
    }

    public void nextRound(){
        actUser = (actUser + 1) % users.size();
        User user = users.get(actUser);
        Server.getInstance().getController().broadcastCommand(new Command(CommandType.MESSAGE, user.getName() + " ist nun an der Reihe."));
        int cubeOne = 0, cubeTwo = 0;
        for (int cubed = 1; cubeOne == cubeTwo; cubed++){
            Random random = new Random();
            cubeOne = 1 + random.nextInt(6);
            cubeTwo = 1 + random.nextInt(6);
            if (cubed == 3 && cubeOne == cubeTwo){
                intoPrison(actUser);
                Server.getInstance().getController().broadcastCommand(new Command(CommandType.MESSAGE, "Weil " + user.getName() + " 3 Päsche hintereinander gewürfelt hat, muss er nun in das Gefängnis."));
                nextRound();
                return;
            }
            int moves = cubeOne + cubeTwo;
            plots[user.getPosition()].removeVisitor(user);
            int newPos = user.move(moves);
            plots[newPos].addVisitor(user);
            Server.getInstance().getController().broadcastCommand(new Command(CommandType.MOVE_PLAYER, String.valueOf(actUser), String.valueOf(newPos)));
            plots[newPos].activateEffect(user);
        }

    }

    public void changeAmountHouses(int plotID, int amount){
        plots[plotID].changeAmountHouse(amount);
    }

    public void changeBalance(int userID, int amount){
        User user = users.get(userID);
        CommandType outputType = user.changeBalance(amount) ? CommandType.ACCEPT : CommandType.REFUSE;
        Server.getInstance().getController().sendCommand(new Command(outputType), user);
    }

    public void intoPrison(int userID){
        User user = users.get(userID);
        plots[user.getPosition()].removeVisitor(user);
        user.setPosition(40);
        plots[40].addVisitor(user);
        Server.getInstance().getController().broadcastCommand(new Command(CommandType.MOVE_PLAYER, String.valueOf(userID), String.valueOf(40)));
    }

}
