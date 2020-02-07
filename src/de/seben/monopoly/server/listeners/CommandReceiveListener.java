package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.events.ServerCommandRecieveEvent;
import de.seben.monopoly.events.UserQuitEvent;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.server.User;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import java.util.ArrayList;

import de.seben.monopoly.events.Event;

public class CommandReceiveListener implements EventListener{

    @Event
    public void onCommandReceive(ServerCommandRecieveEvent event){
        User sender = event.getCommandSender();
        Command command = event.getCommand();
        CommandType cmdType = command.getCmdType();
        ArrayList<String> args = command.getArgs();
        Monopoly.debug("Command from " + (sender.getName() == null ? "UnknownUser" : sender.getName()) + " (" + sender.getID() + ")");
        Monopoly.debug(" -> " + cmdType.name() + " " + String.join(" ", args));
        switch (cmdType){
            case END_OF_ROUND: // args: null
                Server.getInstance().getEngine().nextRound();
                break;
            case CLAIM_PLOT: // args: PlayerID, plotID
                int userID = Integer.parseInt(args.get(0));
                int plotID = Integer.parseInt(args.get(1));
                break;
            case BUILD_HOUSE: // args: plotID
                Server.getInstance().getEngine().changeAmountHouses(Integer.parseInt(args.get(1)), 1);
                break;
            case REMOVE_HOUSE: // args: plotID
                Server.getInstance().getEngine().changeAmountHouses(Integer.parseInt(args.get(0)), -1);
                break;
            case PAY: // args: PlayerID, Höhe des Betrages, Name des Zielspielers
                Server.getInstance().getEngine().changeBalance(Integer.parseInt(args.get(0)), -1 * Integer.parseInt(args.get(1)));
                Server.getInstance().getEngine().changeBalance(Integer.parseInt(args.get(2)), Integer.parseInt(args.get(1)));
                break;
            case EARN: // args: PlayerID, Höhe des Betrages
                Server.getInstance().getEngine().changeBalance(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)));
                break;
            case CHAT: // args: PlayerID, Nachricht
                int id = Integer.parseInt(args.get(0));
                String message = String.join(" ", args.subList(1, args.size() - 1));
                break;
            case MESSAGE:
                //MESSAGE <message>
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
                }
                break;
            case DISCONNECT:
                Server.getInstance().getEvents().executeEvent(new UserQuitEvent(Server.getInstance().getController().getClientConnection(sender), "Requested"));
                break;
            case INFO:
                //INFO <Spieleranzahl> <Spieler als String>
                ArrayList<User> users = Server.getInstance().getEngine().getUsers();
                ArrayList<String> usernames = new ArrayList<>();
                users.forEach((user -> usernames.add(user.getName())));
                Server.getInstance().getController().sendCommand(new Command(CommandType.INFO, users.size() + "/4", String.join(", ", usernames)), sender);
                break;
            default:
                Monopoly.debug("Unknown Command");
        }

    }

}