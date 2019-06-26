package de.seben.monopoly.events;

import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.server.User;

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
    public int getHouses(){
        return plot.getAmountHouses();
    }
    public int getBuildHouses(){
        return getHouses() - before.getAmountHouses();
    }

}