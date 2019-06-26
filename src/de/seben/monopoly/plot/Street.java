package de.seben.monopoly.plot;

import java.awt.Color;

import de.seben.monopoly.main.Monopoly;

public enum Street{

    BROWN(0, "Braun", 2, 40, 40, 0),
    LIGHT_BLUE(1, "Hellblau", 3, 120, 40, 0),
    PURPLE(2, "Lila", 3, 200, 40, 0),
    ORANGE(3, "Orange", 3, 280, 40, 0),
    RED(4, "Rot", 3, 360, 40, 0),
    YELLOW(5, "Gelb", 3, 440, 40, 0),
    GREEN(6, "Grün", 3, 520, 40, 0),
    DARK_BLUE(7, "Dunkelblau", 2, 700, 300, 0);

    private int id;
    private String name;
    private Plot[] plots;
    private int price;
    private int additionalPriceOfLastPlot;
    private int pricePerHouse;

    Street(int id, String name, int amountOfPlots, int price, int additionalPriceOfLastPlot, int pricePerHouse){
        this.id = id;
        this.name = name;
        this.plots = new Plot[amountOfPlots];
        this.price = price;
        this.additionalPriceOfLastPlot = additionalPriceOfLastPlot;
        this.pricePerHouse = pricePerHouse;
    }

    public int getID(){
        return this.id;
    }
    public int getPrice(){
        return this.price;
    }
    public int getAdditionalPrice(){
        return this.additionalPriceOfLastPlot;
    }
    public Color getColor(){
        return Color.getColor(this.toString());
    }
    public void addPlot(Plot plot){
        for(int i = 0; i < plots.length; i++){
            if(plots[i] == null){
                plots[i] = plot;
                return;
            }
        }
        Monopoly.debug("Street " + this.name() + " is already full");
    }

    @Override
    public String toString(){
        return this.name;
    }

    public int getPricePerHouse() {
        return this.pricePerHouse;
    }
}