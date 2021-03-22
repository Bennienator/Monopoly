package de.seben.monopoly.server.listeners;

import de.seben.monopoly.events.structure.Event;
import de.seben.monopoly.events.structure.EventListener;
import de.seben.monopoly.events.UserLoginEvent;
import de.seben.monopoly.server.Server;
import de.seben.monopoly.utils.Command;
import de.seben.monopoly.utils.CommandType;
import de.seben.monopoly.utils.User;

public class UserLoginListener implements EventListener {

	@Event
	public void onUserLogin(UserLoginEvent event){
		User user = event.getUser();
		System.out.println(user.getName() + " (" + user.getID() + ") joined");
		Server.getInstance().getController().broadcastUserList();
		if(Server.getInstance().getEngine().getUsers().size() == 4 && Server.getInstance().getEngine().allUsersReady()){
			Server.getInstance().getController().broadcastCommand(new Command(CommandType.START_ROUND));
		}
	}

}
