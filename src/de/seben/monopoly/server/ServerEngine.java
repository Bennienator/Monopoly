package de.seben.monopoly.server;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

public class ServerEngine {

    private Plot[] plots;
    private User[] users;
    private int actUser;


    public ServerEngine(){
        plots = new Plot[40];
        //TODO: Alle Spielfelder erstellen
        users = new User[4];
    }

    public void startRound(){
        do {
            actUser = (actUser + 1) % 4;
        } while (users[actUser] == null);
        int cube = (int) (Math.random() * 11) + 2;
        int newPos = users[actUser].move(cube);
        ClientController.getInstance().broadcastCommand(new Command(CommandType.MOVE_PLAYER, String.valueOf(users[actUser].move(cube))));
    }



}
