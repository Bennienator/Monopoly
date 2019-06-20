package de.seben.monopoly.server;

import javax.swing.*;
import java.awt.*;

public class User {

    //TODO: Wie kann das Programm ein Icon einfügen?

    private int balance; //Guthaben des Spielers
    private int actPos; //aktuelle Position des Spielers auf dem Spielbrett (= ID des Grundstücks)
    private String name; //Name des Spielers
    private Icon meeple; //Spielfigur des Spielers

    public User(String name){
        this.name = name;
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

    public Icon getIcon(){ return meeple; }
}
