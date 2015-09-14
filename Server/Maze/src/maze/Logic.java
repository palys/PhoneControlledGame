package maze;

import game.InputServer;
import game.Message;
import game.Move;
import game.Player;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Logic {
	
	private enum CollisionCheck {
		WIN, COLLIDES, NOTCOLLIDES
	}
	
	private Player[] players;
	
	private float playerRadius = 0.25f;
	
	private int[][] maze;
	
	private final InputServer input;
	
	private Map<Player, Point2D.Float> playerPosition;
	
	private final static float scalex = 1f;
	
	private static final float scaley = 1f;
	
	private MazeGenerator mazeGenerator;
	
	public Logic(InputServer input) {
		this.input = input;
		this.players = new Player[4];
		this.playerPosition = new HashMap<Player, Point2D.Float>();
		this.mazeGenerator = new SampleMazeGenerator();
	}
	
	public void restart() {
		for (Player p : players) {
			playerPosition.put(p, startingPoint());
		}
	}
	
	public void startGame() {
		maze = mazeGenerator.generateMaze();
	}
	
	private boolean collidesWithUnitSquare(Point2D.Float point, float radius, int x, int y) {
		//Square is [x, x+1] x [y, y+1]
		
		float left = x;
		float top = y;
		float right = x + 1;
		float bottom = y + 1;
		
		//Check if inside
		if (left <= point.x && point.x <= right && top <= point.y && point.y <= bottom) {
			return true;
		}
		
		//Check collisions with corners
		if (point.distance(left, top) < radius) {
			return true;
		}
		
		if (point.distance(left, bottom) < radius) {
			return true;
		}
		
		if (point.distance(right, top) < radius) {
			return true;
		}
		
		if (point.distance(right, bottom) < radius) {
			return true;
		}
		
		//Collision with vertical side
		if (top <= point.y && point.y <= bottom) {
			if (point.x + radius > left && point.x - radius < left) {
				return true;
			}
			
			if (point.x - radius < right && point.x + radius > right) {
				return true;
			}
		}
		
		//Collision with horizontal side
		if (left <= point.x && point.x <= right) {
			if (point.y + radius > top && point.y - radius < top) {
				return true;
			}
			
			if (point.y - radius < bottom && point.y + radius > bottom) {
				return true;
			}
		}
		
		return false;
	}
	
	private CollisionCheck collides(Point2D.Float position, float radius, int[][] maze) {

		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				
				if (maze[y][x] != 0 && maze[y][x] != 3) {
					if (collidesWithUnitSquare(position, radius, x, y)) {
						if (maze[y][x] == 1) {
							return CollisionCheck.COLLIDES;
						} else if (maze[y][x] == 2) {
							return CollisionCheck.WIN;
						}
					}
				}
			}
		}
		
		return CollisionCheck.NOTCOLLIDES;
	}
	
	private void gameEnded(Player winner) {
		//FIXME
		@SuppressWarnings("serial")
		Message message = new Message() {

			@SuppressWarnings("unchecked")
			@Override
			public <T extends Serializable> T getContent() {
				return (T) "Game Over";
			}
			
		};
		input.onGameEnded(winner, message);
		System.out.println("Game ended");
	}
	
	public void onPlayerMoved(Player player, Move move) {

		Move2D move2d = Move2D.of(move, scalex, scaley);
		Point2D.Float oldPos = playerPosition.get(player);
		Point2D.Float newPos = new Point2D.Float(oldPos.x + move2d.getX(), oldPos.y + move2d.getY());
		CollisionCheck check = collides(newPos, playerRadius, maze);
		
		if (check == CollisionCheck.NOTCOLLIDES || check == CollisionCheck.WIN) {
			playerPosition.put(player, newPos);
		} 
		
		if (check == CollisionCheck.WIN) {
			gameEnded(player);
		}
	}
	
	private Point2D.Float startingPoint() {
		
		int x = 0, y = 0;

		for (y = 0; y < maze.length; y++) {
			for (x = 0; x < maze[y].length; x++) {
				if (maze[y][x] == 3) {
					break;
				}
			}
		}
		
		float xf = x + 0.5f;
		float yf = y - 0.5f;
		
		return new Point2D.Float(xf, yf);
	}
	
	@SuppressWarnings("serial")
	public void onPlayerJoined(Player player) {
		
		Message m = null;
		boolean joined = false;
		for (int i = 0; i < players.length; i++) {
		
			if (players[i] == null) {
				
				players[i] = player;
				final int num = i;
				joined = true;
				m = new Message() {
					
					@SuppressWarnings("unchecked")
					@Override
					public <T extends Serializable> T getContent() {
						// TODO Auto-generated method stub
						return (T) ("Game joined. Your color is " + MazePanel.playerColors[num]);
					}
				};
				break;
			}
		}
		
		if (!joined) {
			m = new Message() {
				
				@SuppressWarnings("unchecked")
				@Override
				public <T extends Serializable> T getContent() {
					// TODO Auto-generated method stub
					return (T) "Game is full. Please try again later";
				}
			};
		}
		
		input.sendMesage(player, m);
		
		playerPosition.put(player, startingPoint());
	}
	
	public void onPlayerLeaved(Player player) {
		
		for (int i = 0; i < players.length; i++) {
			if (players[i].equals(player)) {
				players[i] = null;
			}
		}
		
		playerPosition.remove(player);
	}

	public Player[] getPlayers() {
		return players;
	}

	public float getPlayerRadius() {
		return playerRadius;
	}

	public int[][] getMaze() {
		return maze;
	}

	public Map<Player, Point2D.Float> getPlayerPosition() {
		return playerPosition;
	}

}
