package de.seben.monopoly.client.frames;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatFrame extends Thread implements FrameDelegate{

	private JFrame frame = new JFrame("Monopoly - Chat");
	private JTextArea chatTextArea = new JTextArea();

	public ChatFrame(){
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

	}
}
