package de.seben.monopoly.client;

import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class CommandHandler extends Thread{

    private Client client;

    public CommandHandler(Client client){
        this.client = client;
    }

    public void run(){ // Commands vom Server werden bearbeitet
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(client.getSocket().getInputStream());
                Object in;
                if ((in = ois.readObject()) != null) {
                    System.out.println(in.toString());
                    Command input = (Command) in;
                    CommandType cmdType = input.getCmdType();
                    ArrayList<String> args = input.getArgs();
                    switch (cmdType) {
                        case START_ROUND:
                            client.startRound();
                            break;
                        case MOVE_PLAYER:
                            client.movePlayer(args.get(0), Integer.valueOf(args.get(1)));
                            break;
                        case CLAIM_PLOT:
                            client.setOwner(args.get(0), Integer.valueOf(args.get(1)));
                        case BUILD_HOUSE:
                            client.changeAmountHouses(Integer.valueOf(args.get(0)), 1);
                        case REMOVE_HOUSE:
                            client.changeAmountHouses(Integer.valueOf(args.get(0)), -1);
                        case PAY:
                            client.changeCreditPlayer(args.get(0), -1 * Integer.valueOf(args.get(1)));
                        case EARN:
                            client.changeCreditPlayer(args.get(0), Integer.valueOf(args.get(1)));
                        case SET_MONEY:
                            client.setCreditPlayer(args.get(0), Integer.valueOf(args.get(1)));
                        case MESSAGE:
                            JOptionPane.showMessageDialog(null, args.get(0), "Du hast eine Nachricht bekommen!", JOptionPane.INFORMATION_MESSAGE);
                        case CHAT:
                            String message = input.getArgs().get(0);
                            System.out.println("CHAT: " + message);
                            client.addChatMessage(message);
                            break;
                    }
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
    }
}
