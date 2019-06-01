package de.seben.monopoly.server;

import javax.swing.*;
import java.awt.*;

public class User {

    private int money; //Guthaben des Spielers
    private int actPos; //aktuelle Position des Spielers auf dem Spielbrett (= ID des Grundstücks)
    private String name; //Name des Spielers
    private Icon meeple; //Spielfigur des Spielers

    public int move(int moves){
        return actPos = (actPos + moves) % 40;
    }

    public void setPosition(int pos) { actPos = pos; }

    public int getPosition(){ return actPos; }

    public String getName(){ return name; }

    public Icon getIcon(){ return meeple; }
}
