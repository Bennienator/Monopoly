package de.seben.monopoly.plot;

import de.seben.monopoly.server.ClientController;
import de.seben.monopoly.server.User;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

public class BuyPlot implements PlotAction {

    private int pricePlot;

    public BuyPlot(int pricePlot){
        this.pricePlot = pricePlot;
    }

    public void start(User visitor) {
        if (visitor.getBalance() > pricePlot){
            ClientController.getInstance().sendCommand(new Command(CommandType.BUY_PLOT, visitor.getName()));
        }
    }
}
