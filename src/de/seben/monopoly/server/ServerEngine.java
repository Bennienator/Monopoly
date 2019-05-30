package de.seben.monopoly.server;

import de.seben.monopoly.main.Announcements;

public class ServerEngine implements Announcements {

    private Server server;

    public ServerEngine(int actClient){ }

    public void nextRound(String nameOfActClient){
        int dice = (int) (Math.random() * 12) + 1;
        server.broadcastMessage(MP + dice);
    }
}
