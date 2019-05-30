package de.seben.monopoly.server;

import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {

    private ArrayList<User> clients;
    private ServerSocket serverSocket;

    public Server(){
        clients = new ArrayList<User>();
        System.out.println("Servus! Ich bin ein Server.");
    }

    public void broadcastMessage(String message){
        //TODO: implement method
    }

    /*
    Wenn der Inhalt der Variable 'EOR' (Interface 'Announcements') vom aktiven Client gesendet wird, ist der nächste Spieler dran.
     */

}
