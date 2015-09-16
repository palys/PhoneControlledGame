package server;

import java.util.HashMap;

import game.Player;

public class Players {
	
	private static  HashMap<String, Player> players = new HashMap<String, Player>();

	public static Player get(String name) {
		return players.get(name);
	}
	
	public static void put(String name, Player player) {
		players.put(name,  player);
	}
}
