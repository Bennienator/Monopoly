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

	public ArrayList<User> getUsers() {
		return this.users;
	}
}
