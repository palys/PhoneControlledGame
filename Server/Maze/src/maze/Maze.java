package maze;

import game.Game;
import game.InputServer;
import game.Message;
import game.Move;
import game.Player;

import java.util.Scanner;

public class Maze implements Game {
	
	private final InputServer input;
	
	private final Gui gui;
	
	private final Logic logic;
	
	public Maze(InputServer input) {
		this.input = input;
		this.logic = new Logic(this.input);
		this.gui = new Gui(logic);
	}
	
	public void startGame() {
		logic.startGame();
		gui.init();
	}

	@Override
	public void onPlayerJoined(Player player) {
		logic.onPlayerJoined(player);
		gui.refresh();
		
	}

	@Override
	public void onPlayerLeaved(Player player) {
		logic.onPlayerLeaved(player);
		gui.refresh();
		
	}

	@Override
	public void onPlayerMoved(Player player, Move move) {
		logic.onPlayerMoved(player, move);
		gui.refresh();
		
	}
	
	@Override
	public void restart() {
		logic.restart();
		gui.refresh();
	}
	
	public static void main(String[] args) {
		InputServer inputMock = new InputServer() {
			
			@Override
			public void registerGame(Game game) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGameEnded(Player winner, Message message) {
				System.out.println(winner.getName() + ":END " + message);
				
			}
			
			@Override
			public void notifyPlayers(Message message) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void sendMesage(Player to, Message message) {
				System.out.println("Message to " + to.getName() + ": " + message.getContent());
				
			}
		};
		
		Maze maze = new Maze(inputMock);
		maze.startGame();
		Player player = new Player() {

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "Player";
			}
			
			
		};
		maze.onPlayerJoined(player);
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			char c = scanner.next().charAt(0);
			Move m = null;
			if (c == 'w') {
				m = MoveMock.UP;
			} else if (c == 'a') {
				m = MoveMock.LEFT;
			} else if (c == 's') {
				m = MoveMock.DOWN;
			} else if (c == 'd') {
				m = MoveMock.RIGHT;
			}
			System.out.println(m);
			maze.onPlayerMoved(player, m);
		}

	}
	
	private static class MoveMock implements Move {
		
		public static final MoveMock UP = new MoveMock(0.1f, 0.0f, -7.81f, 0.0f);
		public static final MoveMock LEFT = new MoveMock(0.1f, -2.0f, -9.81f, 0.0f);
		public static final MoveMock DOWN = new MoveMock(0.1f, 0.0f, -11.81f, 0.0f);
		public static final MoveMock RIGHT = new MoveMock(0.1f, 2.0f, -9.81f, 0.0f);
		
		float f[] = new float[3];
		float d;
		
		public MoveMock(float d, float x, float y, float z) {
			this.d = d;
			this.f[0] = x;
			this.f[1] = y;
			this.f[2] = z;
		}

		@Override
		public float[] acceleration() {
			// TODO Auto-generated method stub
			return f;
		}

		@Override
		public float durationInSecconds() {
			// TODO Auto-generated method stub
			return d;
		}
		
	}

}
