package game;

public interface InputServer {

	void notifyPlayers(Message message);
	
	void onGameEnded(Player winner, Message message);
	
	void registerGame(Game game);
	
	void sendMesage(Player to, Message message);
	
}
