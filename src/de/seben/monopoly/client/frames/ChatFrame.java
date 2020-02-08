package de.seben.monopoly.client.frames;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;
import de.seben.monopoly.utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatFrame extends Thread implements FrameDelegate{

	private JFrame frame = new JFrame("Monopoly - Chat");
	private JPanel chatPanel = new JPanel();
	private JTextArea chatTextArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(chatTextArea);
	private JPanel buttonPane = new JPanel();
	private JTextField messageField = new JTextField();
	private JComboBox<String> userSelector = new JComboBox<>();
	private JButton sendButton = new JButton("Senden");

	private ArrayList<User> users;

	public ChatFrame(){
		this.users = Client.getInstance().getPlayers().getUsers();

		frame.setSize(700, 900);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		chatTextArea.setEditable(false);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS));
		chatPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		chatPanel.add(scrollPane);

		frame.add(chatPanel, BorderLayout.CENTER);

		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(messageField);
		buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
		userSelector.setEditable(false);
		buttonPane.add(userSelector);
		buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonPane.add(sendButton);

		messageField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {

			}
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if(keyEvent.getKeyCode() == 10)
					sendButton.doClick();
			}
			@Override
			public void keyReleased(KeyEvent keyEvent) {

			}
		});

		sendButton.addActionListener((ae) -> new Thread(() -> {
			String username = (String) userSelector.getSelectedItem();
			String message = messageField.getText();
			if(username != null){
				if(username.equals("Öffentlich")){
					Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.CHAT, message));
				}else{
					Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.PRIVATE_CHAT, String.valueOf(Client.getInstance().getPlayers().getUserByUsername(username).getID()), message));
				}
				messageField.setText("");
			}
		}).start());

		frame.add(buttonPane, BorderLayout.PAGE_END);
		update();

		frame.setVisible(true);
	}

	@Override
	public void run(){

	}

	public void addChatMessage(String sender, String message){
		chatTextArea.append("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + sender + " » " + message + "\n");
	}
	public void addPrivateChatMessage(String sender, String message) {
		chatTextArea.append("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + sender + " *»* " + message + "\n");
	}

	@Override
	public void getInformation(String message) {

	}

	@Override
	public void update() {
		this.users = Client.getInstance().getPlayers().getUsers();
		String prevSelected = String.valueOf(userSelector.getSelectedItem());
		userSelector.removeAllItems();
		userSelector.addItem("Öffentlich");
		if(prevSelected.equals("Öffentlich"))
			userSelector.setSelectedItem(prevSelected);
		for(User user : users) {
			if (!Client.getInstance().getUsername().equalsIgnoreCase(user.getName())) {
				userSelector.addItem(user.getName());
				if(user.getName().equals(prevSelected))
					userSelector.setSelectedItem(prevSelected);
			}
		}
	}
}
