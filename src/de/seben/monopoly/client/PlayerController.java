package de.seben.monopoly.client;

import de.seben.monopoly.main.Monopoly;
import de.seben.monopoly.utils.User;

import java.util.ArrayList;

public class PlayerController {

	private static PlayerController instance;

	public static PlayerController getInstance() {
		if (instance == null)
			instance = new PlayerController();
		return instance;
	}

	private ArrayList<User> users = new ArrayList<>();

	private PlayerController() {
	}

	public void setUsers(ArrayList<User> users){
		Monopoly.debug("Updated playerList");
		this.users = users;
	}

	public User getUserByUsername(String username){
		for(User user : users){
			if(user.getName().equalsIgnoreCase(username))
				return user;
		}
		Monopoly.debug("No user found with username '" + username + "'");
		return null;
	}
	public User getUserByID(int id) {
		for(User user : users){
			if(user.getID() == id)
				return user;
		}
		Monopoly.debug("No user found with id '" + id + "'");
		return null;
	}

	public ArrayList<User> getUsers() {
		return this.users;
	}
}
