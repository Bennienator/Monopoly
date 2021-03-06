package de.seben.monopoly.utils;

import java.awt.*;
import java.io.Serializable;

public class User implements Serializable {

    // TODO: Wie kann das Programm ein Icon einfügen?
    // Muss das ein Icon sein oder kann das auch ein Image sein?

    private int id;         //ID des Spielers (eindeutige Identifikation)
    private String name;    //Name des Spielers
    private int balance;    //Guthaben des Spielers
    private int actPos;     //aktuelle Position des Spielers auf dem Spielbrett (= ID des Grundstücks)
    private Image meeple;   //Spielfigur des Spielers
    private boolean ready;

    public User(){
        this.meeple = Toolkit.getDefaultToolkit().getImage("");
    }
    public User(int id){
        this();
        this.id = id;
    }
    public User(int id, String name){
        this(id);
        this.name = name;
    }
    public User(int id, String name, boolean ready){
        this(id);
        this.name = name;
        this.ready = ready;
    }

    public int move(int moves){
        return actPos = (actPos + moves) % 40;
    }

    public boolean changeBalance(int amount) {
        boolean valid;
        balance += (valid = balance + amount > 0) ? amount : 0;
        return valid;
    }

    public void setBalance(int balance){
        this.balance = balance;
    }

    public int getBalance() { return this.balance; }

    public void setPosition(int pos) { actPos = pos; }

    public int getPosition(){ return this.actPos; }

    public String getName(){ return this.name; }

    public void setName(String name){
        this.name = name;
    }

    public Image getIcon(){ return this.meeple; }

    public int getID() {
        return this.id;
    }

    public int getActPos() {
        return actPos;
    }

    public void setActPos(int actPos) {
        this.actPos = actPos;
    }

    public boolean isReady() {
        return ready;
    }

    public void toggleReady() {
        this.ready = !this.ready;
    }
}
