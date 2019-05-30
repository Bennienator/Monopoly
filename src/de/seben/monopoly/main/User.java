package de.seben.monopoly.main;

import javax.swing.*;
import java.awt.*;

public class User {

    private int money; //Guthaben des Spielers
    private int actPos; //aktuelle Position des Spielers auf dem Spielbrett (= ID des Grundstücks)
    private String name; //Name des Spielers
    private Icon meeple; //Spielfigur des Spielers

    public void move(int moves){
        actPos = (actPos + moves) % 40;
    }

    public int getActPos(){ return actPos; }

    public String getName(){ return name; }

    public Icon getIcon(){ return meeple; }
}
