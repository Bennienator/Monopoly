package de.seben.monopoly.events;

import de.seben.monopoly.plot.Plot;

public class BuildHouseEvent implements IEvent {

    private Plot plot;
    private User builder;
    private Plot before;

    public BuildHouseEvent(Plot plot, User builder, Plot before){
        this.plot = plot;
        this.builder = builder;
        this.before = before;
    }

    public Plot getPlot(){
        return this.plot;
    }
    public User getBuilder(){
        return this.builder;
    }
    public Plot getPlotBeforeBuild(){
        return this.before;
    }
    public int getPricePerHouse(){
        return plot.getStreet().getPricePerHouse();
    }
    public int getBuildHouses(){
        return houseAmount - before.getAmountHouses();
    }
    public int getHouses(){
        return plot.getAmountHouses();
    }

}