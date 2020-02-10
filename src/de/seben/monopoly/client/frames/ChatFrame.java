package de.seben.monopoly.client.frames;

import de.seben.monopoly.client.Client;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;
import de.seben.monopoly.utils.User;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatFrame extends Thread implements FrameDelegate{

	private JFrame frame = new JFrame("Monopoly - Chat");
	private JPanel chatPanel = new JPanel();
	private JTextPane chatTextPane = new JTextPane();
	private JScrollPane scrollPane = new JScrollPane(chatTextPane);
	private JPanel buttonPane = new JPanel();
	private JTextField messageField = new JTextField();
	private JComboBox<String> userSelector = new JComboBox<>();
	private JButton sendButton = new JButton("Senden");

	private ArrayList<User> users;

	public ChatFrame(){
		this.users = Client.getInstance().getPlayers().getUsers();

		frame.setSize(600, 700);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		chatTextPane.setEditable(false);
		chatTextPane.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		((DefaultCaret)chatTextPane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.PAGE_AXIS));
		chatPanel.add(scrollPane);
		chatPanel.add(Box.createRigidArea(new Dimension(0,5)));

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
			if(username != null && !message.isEmpty()){
				if(username.equals("Öffentlich")){
					Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.CHAT, message));
				}else{
					Client.getInstance().getHandler().sendCommandToServer(new Command(CommandType.PRIVATE_CHAT, String.valueOf(Client.getInstance().getPlayers().getUserByUsername(username).getID()), message));
				}
				messageField.setText("");
			}
		}).start());

		frame.add(buttonPane, BorderLayout.PAGE_END);
		frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		update();

		frame.setVisible(true);
	}

	@Override
	public void run(){

	}

	public void addChatMessage(String sender, String message){

		try {
			Document doc = chatTextPane.getStyledDocument();

			SimpleAttributeSet dateSet = new SimpleAttributeSet();
			StyleConstants.setForeground(dateSet, Color.DARK_GRAY);
			doc.insertString(doc.getLength(), "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] ", dateSet);

			SimpleAttributeSet nameSet = new SimpleAttributeSet();
			StyleConstants.setBold(nameSet, true);
			StyleConstants.setForeground(nameSet, Color.BLACK);
			doc.insertString(doc.getLength(), sender, nameSet);

			doc.insertString(doc.getLength(), " » ", dateSet);

			SimpleAttributeSet messageSet = new SimpleAttributeSet();
			StyleConstants.setItalic(messageSet, true);
			StyleConstants.setForeground(messageSet, Color.BLACK);
			doc.insertString(doc.getLength(), message + "\n", messageSet);

		}catch (BadLocationException e){
			e.printStackTrace();
		}

	}
	public void addPrivateChatMessage(String sender, String message) {
		try {
			Document doc = chatTextPane.getStyledDocument();

			SimpleAttributeSet dateSet = new SimpleAttributeSet();
			StyleConstants.setBackground(dateSet, Color.LIGHT_GRAY);
			StyleConstants.setForeground(dateSet, Color.DARK_GRAY);
			doc.insertString(doc.getLength(), "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] ", dateSet);

			SimpleAttributeSet nameSet = new SimpleAttributeSet();
			StyleConstants.setBold(nameSet, true);
			StyleConstants.setBackground(nameSet, Color.LIGHT_GRAY);
			StyleConstants.setForeground(nameSet, Color.BLACK);
			doc.insertString(doc.getLength(), sender, nameSet);

			doc.insertString(doc.getLength(), " » ", dateSet);

			SimpleAttributeSet messageSet = new SimpleAttributeSet();
			StyleConstants.setItalic(messageSet, true);
			StyleConstants.setBackground(messageSet, Color.LIGHT_GRAY);
			StyleConstants.setForeground(messageSet, Color.BLACK);
			doc.insertString(doc.getLength(), message + "\n", messageSet);

		}catch (BadLocationException e){
			e.printStackTrace();
		}
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
