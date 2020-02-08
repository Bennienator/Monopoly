package de.seben.monopoly.client.frames;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatFrame implements FrameDelegate{

	private JTextArea chatTextArea = new JTextArea();

	public ChatFrame(){

	}

	public void addChatMessage(String sender, String message){
		chatTextArea.append("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + sender + " » " + message + "\n");
	}

	@Override
	public void getInformation(String message) {

	}

	@Override
	public void update() {

	}
}
