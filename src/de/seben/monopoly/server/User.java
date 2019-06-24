package de.seben.monopoly.server;

import java.awt.*;

public class User {

    //TODO: Wie kann das Programm ein Icon einfügen?
    //Muss das ein Icon sein oder kann das auch ein Image sein?

    private int balance; //Guthaben des Spielers
    private int actPos; //aktuelle Position des Spielers auf dem Spielbrett (= ID des Grundstücks)
    private String name; //Name des Spielers
    private Image meeple; //Spielfigur des Spielers

    public User(String name){
        this.name = name;
        this.meeple = Toolkit.getDefaultToolkit().getImage("");
    }

    public int move(int moves){
        return actPos = (actPos + moves) % 40;
    }

    public boolean changeBalance(int amount) {
        boolean valid;
        balance += (valid = balance + amount > 0) ? amount : 0;
        return valid;
    }

    public int getBalance() { return balance; }

    public void setPosition(int pos) { actPos = pos; }

    public int getPosition(){ return actPos; }

    public String getName(){ return name; }

    public Image getIcon(){ return meeple; }
}
