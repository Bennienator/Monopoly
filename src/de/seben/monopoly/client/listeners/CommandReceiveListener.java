package de.seben.monopoly.client.listeners;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.events.ClientCommandReceiveEvent;
import de.seben.monopoly.events.Event;
import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;
import de.seben.monopoly.utils.User;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CommandReceiveListener implements EventListener{

    @Event
    public void onCommandReceive(ClientCommandReceiveEvent event){
        Command command = event.getCommand();
        CommandType cmdType = command.getCmdType();
        ArrayList<String> args = event.getArgs();
        Monopoly.debug("Server: " + cmdType.name() + (args.size() > 0 ? " " + String.join(" ", args) : ""));
        switch (cmdType) {
            case ACCEPT:
                if(event.getLastCommand().getCmdType().equals(CommandType.LOGIN)){
                    Monopoly.debug("Logged in as '" + event.getLastCommand().getArgs().get(0) + "'");
                    Client.getInstance().setUsername(event.getLastCommand().getArgs().get(0));
                    Client.getInstance().loggedIn();
                }
                break;
            case REFUSE:
                if(event.getLastCommand().getCmdType().equals(CommandType.LOGIN)){
                    Monopoly.debug("Username is already taken");
                    String username = JOptionPane.showInputDialog(null, "Der gewünschte Benutzername ist bereits vergeben!\nBitte gebe einen anderen Benutzernamen ein.", "Monopoly - Login", JOptionPane.QUESTION_MESSAGE);
                    Monopoly.debug("Logging in as '" + username + "'");
                    Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.LOGIN, username));
                }
                break;
            case PLAYERLIST:
                try {
                    ArrayList<User> users = new ArrayList<>();
                    for (int i = 0; i <= args.size() - 2; i += 2) {
                        users.add(new User(Integer.parseInt(args.get(i)), args.get(i + 1)));
                    }
                    Client.getInstance().getPlayers().setUsers(users);
                    Client.getInstance().getConnectFrame().update();
                    Client.getInstance().getChatFrame().update();
                }catch (NumberFormatException e){
                    Monopoly.debug("Error receiving playerList");
                }
                break;
            case START_ROUND:
                Client.getInstance().startRound();
                break;
            case MOVE_PLAYER:
                Client.getInstance().movePlayer(args.get(0), Integer.parseInt(args.get(1)));
                break;
            case CLAIM_PLOT:
                Client.getInstance().setOwner(args.get(0), Integer.parseInt(args.get(1)));
                break;
            case BUILD_HOUSE:
                Client.getInstance().changeAmountHouses(Integer.parseInt(args.get(0)), 1);
                break;
            case REMOVE_HOUSE:
                Client.getInstance().changeAmountHouses(Integer.parseInt(args.get(0)), -1);
                break;
            case BUY_PLOT:
                int choice = JOptionPane.showConfirmDialog(null, "Du hast die Möglichkeit, das Grundstück '" + args.get(1) + "' zu kaufen. Es kostet " + args.get(2) + ".\nMöchtest du dieses Grundstück kaufen?", "Grundstück verfügbar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == 0){
                    Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.PAY, args.get(2)));
                }else{
                    Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.REFUSE));
                }
                break;
            case PAY:
                Client.getInstance().changeCreditPlayer(args.get(0), -1 * Integer.parseInt(args.get(1)));
                break;
            case EARN:
                Client.getInstance().changeCreditPlayer(args.get(0), Integer.parseInt(args.get(1)));
                break;
            case SET_MONEY:
                Client.getInstance().setCreditPlayer(args.get(0), Integer.parseInt(args.get(1)));
                break;
            case BROADCAST:
                JOptionPane.showMessageDialog(null, args.get(0), "Du hast eine Nachricht bekommen!", JOptionPane.INFORMATION_MESSAGE);
                break;
            case CHAT:  // args: senderName, message
                System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + args.get(0) + " » " + args.get(1));
                Client.getInstance().addChatMessage(args.get(0), args.get(1));
                break;
            case PRIVATE_CHAT:  // args: senderName, message
                System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH.mm.ss")) + "] " + args.get(0) + " *»* " + args.get(1));
                Client.getInstance().addPrivateChatMessage(args.get(0), args.get(1));
                break;
            case KICK:
                Client.getInstance().disconnect();
                System.out.println("You were kicked by Server");
                break;
            case DISCONNECT:
                Client.getInstance().disconnect();
                break;
            default:
                Monopoly.debug("Unknown command");
        }

    }

}