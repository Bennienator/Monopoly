package de.seben.monopoly.client.listeners;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.events.ClientCommandRecieveEvent;
import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.events.Event;
import de.seben.monopoly.utils.Command;

public class CommandRecieveListener implements EventListener{

    @Event
    public void onCommandRecieve(ClientCommandRecieveEvent event){
        Command command = event.getCommand();
        CommandType cmdType = command.getCmdType();
        ArrayList<String> args = event.getArgs();
        Monopoly.debug("Server: " + cmdType.name() + " " + String.join(" ", args));
        switch (cmdType) {
            case LOGIN:
                
            case START_ROUND:
                Client.getInstance().startRound();
                break;
            case MOVE_PLAYER:
                Client.getInstance().movePlayer(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case CLAIM_PLOT:
                Client.getInstance().setOwner(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case BUILD_HOUSE:
                Client.getInstance().changeAmountHouses(Integer.valueOf(args.get(0)), 1);
                break;
            case REMOVE_HOUSE:
                Client.getInstance().changeAmountHouses(Integer.valueOf(args.get(0)), -1);
                break;
            case BUY_PLOT:
                int choice = JOptionPane.showConfirmDialog(null, "Du hast die Möglichkeit, das Grundstück '" + args.get(1) + "' zu kaufen. Es kostet " + args.get(2) + ".\nMöchtest du dieses Grundstück kaufen?", "Grundstück verfügbar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == 0){
                    Client.getInstance().getHandler().sendCommand(new Command(CommandType.PAY, args.get(2)));
                }
                break;
            case PAY:
                Client.getInstance().changeCreditPlayer(args.get(0), -1 * Integer.valueOf(args.get(1)));
                break;
            case EARN:
                Client.getInstance().changeCreditPlayer(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case SET_MONEY:
                Client.getInstance().setCreditPlayer(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case MESSAGE:
                JOptionPane.showMessageDialog(null, args.get(0), "Du hast eine Nachricht bekommen!", JOptionPane.INFORMATION_MESSAGE);
                break;
            case CHAT:
                Monopoly.debug("CHAT: " + args.get(0));
                Client.getInstance().addChatMessage(args.get(0));
                break;
            case DISCONNECT:
                Client.getInstance().disconnect();
            default:
                Monopoly.debug("Unknown command");
        }

    }

}