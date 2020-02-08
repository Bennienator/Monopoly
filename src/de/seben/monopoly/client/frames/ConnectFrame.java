package de.seben.monopoly.client.frames;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.utils.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ConnectFrame implements FrameDelegate {

    private JFrame frame = new JFrame("Monopoly");

    private JPanel welcomePanel = new JPanel();
    private JLabel welcome = new JLabel("Willkommen");
    private JLabel welcome_2 = new JLabel("beim besten Monopoly der ganzen Welt!");

    private JPanel usersPanel = new JPanel();
    private ArrayList<User> users;
    private JLabel[] labels = new JLabel[4];

    private JPanel statusPanel = new JPanel();
    private JLabel status = new JLabel("Warte auf Spieler...");


    public ConnectFrame(){

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());

        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome_2.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.PAGE_AXIS));
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        Font font = welcome.getFont();
        welcome.setFont(new Font(font.getName(), font.getStyle(), 20));
        welcome_2.setFont(new Font(font.getName(), font.getStyle(), 15));

        welcomePanel.add(welcome);
        welcomePanel.add(Box.createRigidArea(new Dimension(0,5)));
        welcomePanel.add(welcome_2);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        frame.getContentPane().add(welcomePanel, BorderLayout.PAGE_START);

        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.PAGE_AXIS));
        usersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        for(int i = 0; i < 4; i++) {
            labels[i] = new JLabel("Spieler " + (i+1) + ": ");
            labels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            usersPanel.add(labels[i]);
            usersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        usersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        frame.getContentPane().add(usersPanel, BorderLayout.CENTER);

        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.PAGE_AXIS));
        statusPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        statusPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.add(status);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        frame.getContentPane().add(statusPanel, BorderLayout.PAGE_END);

        update();


        frame.setVisible(true);

    }

    @Override
    public void getInformation(String message) {

    }

    @Override
    public void update() {

        this.users = Client.getInstance().getPlayers().getUsers();

        for(int i = 0; i < 4; i++){
            labels[i].setForeground(new JLabel().getForeground());
            labels[i].setText("Spieler " + (i+1) + ": ");
        }
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equalsIgnoreCase(Client.getInstance().getUsername()))
                labels[i].setForeground(Color.decode("#006400"));
            labels[i].setText("Spieler " + (i+1) + ": " + users.get(i).getName());

        }


        status.setText("Warte auf " + (4 - users.size()) + " weitere Spieler...");

    }
}
