package de.seben.monopoly.server;

import de.seben.monopoly.events.UserQuitEvent;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;
import de.seben.monopoly.utils.User;

import java.util.ArrayList;
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
        User user = getUserByID(actUser);
        if(user != null) {
            Server.getInstance().getController().broadcastCommand(new Command(CommandType.BROADCAST, user.getName() + " ist nun an der Reihe."));
            int cubeOne = 0, cubeTwo = 0;
            for (int diced = 1; cubeOne == cubeTwo; diced++) {
                Random random = new Random();
                cubeOne = 1 + random.nextInt(6);
                cubeTwo = 1 + random.nextInt(6);
                if (diced == 3 && cubeOne == cubeTwo) {
                    intoPrison(user);
                    Server.getInstance().getController().broadcastCommand(new Command(CommandType.BROADCAST, "Weil " + user.getName() + " 3 Päsche hintereinander gewürfelt hat, muss er nun in das Gefängnis."));
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
    }

    public User getUserByUsername(String username){
        for(User user : users.values()){
            if(user.getName().equalsIgnoreCase(username))
                return user;
        }
        return null;
    }
    public User getUserByID(int id) {
        if(users.containsKey(id)) {
            return users.get(id);
        }
        Monopoly.debug("No user found with id '" + id + "'");
        return null;
    }

    public void changeAmountHouses(int plotID, int amount){
        plots[plotID].changeAmountHouse(amount);
    }

    public void changeBalance(User user, int amount){
        if(!userExists(user)) return;

        CommandType outputType = user.changeBalance(amount) ? CommandType.ACCEPT : CommandType.REFUSE;
        Server.getInstance().getController().sendCommand(new Command(outputType), user);
    }

    public void intoPrison(User user){
        if(!userExists(user)) return;

        plots[user.getPosition()].removeVisitor(user);
        user.setPosition(40);
        plots[40].addVisitor(user);
        Server.getInstance().getController().broadcastCommand(new Command(CommandType.MOVE_PLAYER, String.valueOf(user.getID()), String.valueOf(40)));
    }

    public boolean kickUser(int userID) {
        ClientConnection connection = Server.getInstance().getController().getClientConnection(userID);
        return kickUser(connection);
    }

    public boolean kickUser(String username) {
        User user = getUserByUsername(username);
        if(user != null) {
            ClientConnection connection = Server.getInstance().getController().getClientConnection(user);
            return kickUser(connection);
        }
        return false;
    }
    public boolean kickUser(User user){
        ClientConnection connection = Server.getInstance().getController().getClientConnection(user);
        return kickUser(connection);
    }

    public boolean kickUser(ClientConnection connection){
        if(connection != null){
            connection.sendCommand(new Command(CommandType.KICK));
            connection.close();
            Server.getInstance().getEvents().executeEvent(new UserQuitEvent(connection, "Kicked"));
            return true;
        }
        return false;
    }

    private boolean userExists(User user){
        if(users.containsValue(user)){
            return true;
        }else{
            Monopoly.debug("User not found");
            return false;
        }
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public void userChat(User sender, String message) {
        Server.getInstance().getController().broadcastCommand(new Command(CommandType.CHAT, sender.getName(), message));
    }

	public void userPrivateChat(User sender, User receiver, String message) {
        Server.getInstance().getController().sendCommand(new Command(CommandType.PRIVATE_CHAT, String.valueOf(sender.getID()), message), receiver);
	}
}
