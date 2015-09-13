package game;

public interface Game {

	void onPlayerJoined(Player player);
	
	void onPlayerLeaved(Player player);
	
	void onPlayerMoved(Player player, Move move);
	
	void restart();
}
