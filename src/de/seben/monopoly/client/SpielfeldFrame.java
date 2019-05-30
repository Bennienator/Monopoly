package de.seben.monopoly.client;

import de.seben.monopoly.server.Plot;
import de.seben.monopoly.server.User;

import javax.swing.*;

public class SpielfeldFrame extends JFrame implements FrameDelegate {

    private static SpielfeldFrame instance = null;

    private JLabel[] plotLabel;

    private int[] X, Y; //KOS-Punkte
    private int W1; //Breite eines Grundstück-Feldes
    private int H1; //Höhe eines Grundstück-Feldes
    private int width; //Breite von Fenster
    private int height; //Höhe von Fenster

    private User actUser; //Person, welche dieses Programm benutzt
    private User[] users; //alle Spieler
    private Plot[] plots; //alle Grundstücke, Aktionsfelder etc.

    private JLabel[][] placesForMeeples; //Stellen für die Spielfiguren

    private SpielfeldFrame(User[] users){ //Konstruktor
        this.users = users;
    }

    public static void createInstance(User[] users){ //Singleton-Pattern -> Fenster erstellen
        instance = new SpielfeldFrame(users);
    }

    public static SpielfeldFrame getInstance(){ //Singleton-Pattern -> Fenster zurückgeben
        return instance;
    }

    private void setPoints(){ //KOS aufbauen //TODO: Größe der Grundstück-Felder müssen angepasst werden, wenn der Bildschirm zu klein ist

    }

    private void setup(){ //Elemente platzieren
        setSize(width, height);
        setIconImage(null); //TODO: IconImage
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Monopoly");
        setResizable(false);
        setLayout(null);

        plotLabel = new JLabel[40];
        for (int i = 0; i < plotLabel.length; i++){
            plotLabel[i] = new JLabel();
        }
        int xStart = 0, yStart = 0; //Index für Startfeld
        plotLabel[0].setBounds(X[xStart], Y[yStart], H1, H1);
        for (int i = 1; i < 10; i++){
            plotLabel[i].setBounds(X[xStart + i], Y[yStart], W1, H1);
        }
        plotLabel[10].setBounds(X[xStart + 10], Y[yStart], H1, H1);
        for (int i = 1; i < 10; i++){
            plotLabel[i + 10].setBounds(X[xStart + 10], Y[yStart + i], H1, W1);
        }
        plotLabel[20].setBounds(X[xStart + 10], Y[yStart + 10], H1, H1);
        for (int i = 1; i < 10; i++){
            plotLabel[i + 20].setBounds(X[xStart + 10 - i], Y[yStart + 10], W1, H1);
        }
        plotLabel[30].setBounds(X[xStart], Y[yStart + 10], H1, H1);
        for (int i = 1; i < 10; i++){
            plotLabel[i + 30].setBounds(X[xStart], Y[yStart + 10 - i], H1, W1);
        }
        //TODO: Bilder der Spielfelder einfügen
        for (int i = 0; i < plotLabel.length; i++){
            getContentPane().add(plotLabel[i]);
        }

        JLabel logoLabel = new JLabel(); //TODO: Logo des Spieles einfügen
        logoLabel.setBounds(X[xStart + 1], Y[yStart + 1], 10 * W1, 10 * W1);
        getContentPane().add(logoLabel);

        setVisible(true);
    }

    public void moveUser(int userID, int moves){ //Spieler mit dem Index 'userID' soll um 'moves' Schritte weiterbewegt werden
        plots[users[userID].getActPos()].removeVisitor(users[userID]);
        users[userID].move(moves);
        plots[users[userID].getActPos()].addVisitor(users[userID]);
        //TODO: Aktion des Feldes ausführen
    }

    public void showPlayerAtPlot(int plotID, int pos, Icon meeple){ //Position 'pos' beim Grundstück 'plotID' soll eine Spielfigur mit der Farbe 'colorOfPlayer' anzeigen
        placesForMeeples[plotID][pos].setIcon(meeple);
    }

    public User getActUser(){ return actUser; }

    public void getInformation(String message){
        
    }
}
