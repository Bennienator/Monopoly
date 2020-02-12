package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.*;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.utils.User;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.util.ArrayList;

public class CommandReceiveListener implements EventListener{

    @Event
    public void onCommandReceive(ServerCommandReceiveEvent event){
        User sender = event.getCommandSender();
        Command command = event.getCommand();
        CommandType cmdType = command.getCmdType();
        ArrayList<String> args = command.getArgs();
        Monopoly.debug("Command from " + (sender.getName() == null ? "UnknownUser" : sender.getName()) + " (" + sender.getID() + ")");
        Monopoly.debug(" -> " + cmdType.name() + " " + String.join(" ", args));

        String message;
        int plotID;
        int amount;
        int receiverID;
        
        switch (cmdType){
            case END_OF_ROUND: // args: null
                Server.getInstance().getEngine().nextRound();
                break;
            case CLAIM_PLOT: // args: plotID
                plotID = Integer.parseInt(args.get(0));
                break;
            case BUILD_HOUSE: // args: plotID
                plotID = Integer.parseInt(args.get(0));
                Server.getInstance().getEngine().changeAmountHouses(plotID, 1);
                break;
            case REMOVE_HOUSE: // args: plotID
                plotID = Integer.parseInt(args.get(0));
                Server.getInstance().getEngine().changeAmountHouses(plotID, -1);
                break;
            case PAY: // args: amount, receiverID
                try {
                    amount = Integer.parseInt(args.get(0));
                    try{
                        receiverID = Integer.parseInt(args.get(1));
                        User receiver = Server.getInstance().getEngine().getUserByID(receiverID);
                        if(receiver != null) {
                            Server.getInstance().getEngine().changeBalance(sender, -1 * amount);
                            Server.getInstance().getEngine().changeBalance(receiver, amount);
                        }
                    }catch (NumberFormatException e){
                        Monopoly.debug("Wrong receiverID");
                    }
                }catch (NumberFormatException e) {
                    Monopoly.debug("Wrong amount");
                }
                break;
            case EARN: // args: userID, amount
                try {
                    amount = Integer.parseInt(args.get(0));
                    Server.getInstance().getEngine().changeBalance(sender, amount);
                }catch (NumberFormatException e){
                    Monopoly.debug("Wrong amount");
                }
                break;
            case CHAT: // args: message
                message = String.join(" ", args);
                Server.getInstance().getEngine().userChat(sender, message);
                break;
            case PRIVATE_CHAT: // args: receiverID, message
                try {
                    receiverID = Integer.parseInt(args.get(0));
                    User receiver = Server.getInstance().getEngine().getUserByID(receiverID);
                    if(receiver != null) {
                        message = String.join(" ", args.subList(1, args.size()));
                        Server.getInstance().getEngine().userPrivateChat(sender, receiver, message);
                    }
                }catch (NumberFormatException e){
                    Monopoly.debug("userID not a number");
                }
                break;
            case LOGIN:
                String username = args.get(0);
                if(Server.getInstance().getController().isUsernameExisting(username)){
                    Monopoly.debug("Requested username already taken");
                    Server.getInstance().getController().sendCommand(new Command(CommandType.REFUSE), sender);
                }else{
                    sender.setName(username);
                    Monopoly.debug("Set name from Client " + sender.getID() + " to '" + username + "'");
                    Server.getInstance().getController().sendCommand(new Command(CommandType.ACCEPT), sender);
                    Server.getInstance().getEvents().executeEvent(new UserLoginEvent(sender));
                }
                break;
            case DISCONNECT:
                Server.getInstance().getController().getClientConnection(sender).sendCommand(new Command(CommandType.ACCEPT));
                Server.getInstance().getEvents().executeEvent(new UserQuitEvent(Server.getInstance().getController().getClientConnection(sender), "Requested"));
                break;
            case READY: // args: boolean
                sender.toggleReady();
                Monopoly.debug(sender.getName() + " is " + (sender.isReady() ? "now" : "not") + " ready");
                Server.getInstance().getController().sendCommand(new Command(CommandType.READY, String.valueOf(sender.isReady())), sender);
                Server.getInstance().getController().broadcastUserList();
                if(Server.getInstance().getEngine().allUsersReady()){
                    Server.getInstance().getController().broadcastCommand(new Command(CommandType.START_ROUND));
                }
                break;
            default:
                Monopoly.debug("Unknown Command");
        }

    }

}