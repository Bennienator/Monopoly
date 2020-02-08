package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.Event;
import de.seben.monopoly.events.EventListener;
import de.seben.monopoly.events.UserLoginEvent;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.utils.User;

public class UserLoginListener implements EventListener {

	@Event
	public void onUserLogin(UserLoginEvent event){
		User user = event.getUser();
		System.out.println(user.getName() + " (" + user.getID() + ") joined");
		Server.getInstance().getController().broadcastUserList();
		if(Server.getInstance().getEngine().getUsers().size() == 4){
			//TODO: START GAME
		}
	}

}
