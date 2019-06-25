package de.seben.monopoly.plot;

import de.seben.monopoly.server.User;

import java.util.ArrayList;

public class Plot {

    private int id; //Position des Grundstücks auf dem Spielbrett
    private Street street; //Farbe der Straße
    private int[] rents; //Alle möglichen Mieten
    private int amountHouses; //Zeiger für die aktuelle Miete
    private String name; //Name des Grundstücks
    private int[][] posForPlaces; //Position der Platzierung für eine Spielfigur ("Vektor", beginnend von der Ecke oben links des Grundstücks)
    private User[] shownVisitors; //angezeigte Besucher
    private ArrayList<User> visitors; //alle Besucher
    private User owner;
    private PlotAction effect; //auszuführender Effekt beim Betreten des Geländes

    public Plot(int id, Street street, int[] rents, String name, int[][] posForPlaces, PlotAction effect){
        this.id = id;
        this.street = street;
        this.street.addPlot(this);
        this.rents = rents;
        this.amountHouses = 0;
        this.name = name;
        this.posForPlaces = posForPlaces;
        this.shownVisitors = new User[posForPlaces.length];
        this.visitors = new ArrayList<>();
        this.effect = effect;
    }

    public void addVisitor(User user){ //Neuen Besucher des Grundstücks (user) hinzufügen -> Spieler kommt grad an
        int pos = -1;
        for (int i = 0; i < shownVisitors.length; i++){
            if (shownVisitors[i] == null){
                pos = i;
                break;
            }
        }
        visitors.add(user);
        if (pos == -1){
            if (true){ //TODO: Überprüfung, ob der Spieler der aktuelle Spieler ist
                shownVisitors[0] = user;
                //TODO: Spielfigur an der Position 0 der Besucher-Anzeige anzeigen lassen
            } else {
                //TODO: Anzeige der '+... Spieler' beim Grundstück um 1 erhöhen
            }
        } else {
            shownVisitors[pos] = user;
            //TODO: Spielfigur an der Position 'pos' der Besucher-Anzeige anzeigen lassen
        }
    }

    public void removeVisitor(User user){ //ehemaligen Besucher des Grundstücks (user) entfernen -> Spieler ist weitergezogen
        visitors.remove(user);
        int oldPos = getPosForShownVisitor(user);
        if (oldPos != -1){
            if (visitors.size() >= shownVisitors.length){
                for (User visitor : visitors) {
                    int hiddenPlayer = getPosForShownVisitor(visitor);
                    if (hiddenPlayer == -1) {
                        shownVisitors[oldPos] = visitor;
                        //TODO: neuen Spieler an der Position 'oldPos' aktualisieren lassen
                        return;
                    }
                }
            } else {
                shownVisitors[oldPos] = null;
                //TODO: Spielfigur an der Position 'oldPos' entfernen lassen
            }
        }
    }

    public boolean changeAmountHouse(int amount){
        if (amountHouses + amount <= 0 || amountHouses + amount >= rents.length){
            return false;
        }
        amountHouses += amount;
        return true;
    }

    public void activateEffect(User visitor){
        effect.start(visitor);
    }

    private int getPosForShownVisitor(User user){ //Position beim Grundstück des angezeigten Spieler zurückgeben (falls nicht angezeigt: -1)
        for (int i = 0; i < shownVisitors.length; i++){
            if (shownVisitors[i].getName().equals(user.getName())){
                return i;
            }
        }
        return -1;
    }

    public User getOwner(){ return owner; }

    public void setAmountHouses(int amount){ this.amountHouses = amount; }
    public int getAmountHouses(){
        return this.amountHouses;
    }
    public int getID(){
        return this.id;
    }
    public PlotAction getEffect(){
        return this.effect;
    }
    public Street getStreet(){
        return this.street;
    }
}
