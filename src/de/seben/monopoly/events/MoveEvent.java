package de.seben.monopoly.events;

import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.plot.PlotAction;

public class MoveEvent implements IEvent {

    private User user;
    private Plot from;
    private Plot to;
    private int distance;
    private PlotAction effect;

    public MoveEvent(User user, Plot from, Plot to){
        this.user = user;
        this.from = from;
        this.to = to;
        this.distance = from.getID() < to.getID() ? to.getID()+40 - from.getID() : to.getID() - from.getID();
        this.effect = to.getEffect();
    }

}