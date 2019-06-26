package de.seben.monopoly.events;

import de.seben.monopoly.plot.Plot;
import de.seben.monopoly.plot.PlotAction;
import de.seben.monopoly.server.User;

public class MoveEvent implements IEvent {

    private User user;
    private Plot from;
    private Plot to;
    private int distance;
    private PlotAction action;

    public MoveEvent(User user, Plot from, Plot to){
        this.user = user;
        this.from = from;
        this.to = to;
        this.distance = from.getID() < to.getID() ? to.getID()+40 - from.getID() : to.getID() - from.getID();
        this.action = to.getEffect();
    }

    public User getUser(){
        return this.user;
    }
    public Plot getFrom(){
        return this.from;
    }
    public Plot getTo(){
        return this.to;
    }
    public int distance(){
        return this.distance;
    }
    public PlotAction PlotAction(){
        return this.action;
    }

}