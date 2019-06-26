package de.seben.monopoly.client;

import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CommandHandler extends Thread{

    private Client client;

    public CommandHandler(Client client){
        Monopoly.debug("Created instance");
        this.client = client;
    }

    public void run(){ // Commands vom Server werden bearbeitet
        Monopoly.debug("Waiting for incomming commands");
        while (client.getSocket().isConnected()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
                Object in;
                if ((in = ois.readObject()) != null) {
                    Command input = (Command) in;
                    Monopoly.debug("Server: " + input.getCmdType().name() + String.join(" ", input.getArgs()));
                    analyseIncommingCommand(input);
                }
            }catch (EOFException e){
                try {
                    sleep(10);
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        Monopoly.debug("Disconnected");
    }

    public void analyseIncommingCommand(Command command){
        CommandType cmdType = command.getCmdType();
        ArrayList<String> args = command.getArgs();
        switch (cmdType) {
            case LOGIN:

            case START_ROUND:
                client.startRound();
                break;
            case MOVE_PLAYER:
                client.movePlayer(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case CLAIM_PLOT:
                client.setOwner(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case BUILD_HOUSE:
                client.changeAmountHouses(Integer.valueOf(args.get(0)), 1);
                break;
            case REMOVE_HOUSE:
                client.changeAmountHouses(Integer.valueOf(args.get(0)), -1);
                break;
            case BUY_PLOT:
                int choice = JOptionPane.showConfirmDialog(null, "Du hast die Möglichkeit, das Grundstück '" + args.get(1) + "' zu kaufen. Es kostet " + args.get(2) + ".\nMöchtest du dieses Grundstück kaufen?", "Grundstück verfügbar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == 0){
                    sendCommand(new Command(CommandType.PAY, args.get(2)));
                }
                break;
            case PAY:
                client.changeCreditPlayer(args.get(0), -1 * Integer.valueOf(args.get(1)));
                break;
            case EARN:
                client.changeCreditPlayer(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case SET_MONEY:
                client.setCreditPlayer(args.get(0), Integer.valueOf(args.get(1)));
                break;
            case MESSAGE:
                JOptionPane.showMessageDialog(null, args.get(0), "Du hast eine Nachricht bekommen!", JOptionPane.INFORMATION_MESSAGE);
                break;
            case CHAT:
                Monopoly.debug("CHAT: " + args.get(0));
                client.addChatMessage(args.get(0));
                break;
            case DISCONNECT:
                client.disconnect();
            default:
                Monopoly.debug("Unknown command");
        }
    }

    public void sendCommand(Command output){
        try {
            if (client.getSocket() != null) {
                ObjectOutputStream oos = new ObjectOutputStream(client.getSocket().getOutputStream());
                oos.writeObject(output);
                Monopoly.debug("Sending: " + output.getCmdType().name() + " " + String.join(" ", output.getArgs()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
