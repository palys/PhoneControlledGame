package maze;

import game.InputServer;
import game.Message;
import game.Move;
import game.Player;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import maze.randomGenerator.RandomMazeGeneratorBuilder;

public class Logic {

	private enum CollisionCheck {
		WIN, COLLIDES, NOTCOLLIDES
	}

	private Player[] players;

	private float playerRadius = 0.25f;

	private int[][] maze;

	private final InputServer input;

	private Map<Player, Point2D.Float> playerPosition;

	private Map<Player, Integer> playerPoints;

	private Map<Player, Long> previousCollision;

	private final static float scalex = 1f;

	private static final float scaley = 1f;

	private MazeGenerator mazeGenerator;

	private final float step = 0.1f;

	private final static int POINTS_SUCCESS = 1000;

	private final static int POINTS_COLLISION = 10;

	private final static long TIME_BETWEEN_COLLISIONS = 500l;

	private final static float eps = 1e-3f;

	public Logic(InputServer input) {
		this.input = input;
		this.players = new Player[4];
		this.playerPosition = new HashMap<Player, Point2D.Float>();
		this.playerPoints = new HashMap<Player, Integer>();
		this.previousCollision = new HashMap<Player, Long>();
		this.mazeGenerator = new RandomMazeGeneratorBuilder()
				.width(15)
				.height(15)
				.startPoint(new Point(7, 14))
				.endPoint(new Point(7, 0))
				.build();
	}

	public void restart() {
		maze = mazeGenerator.generateMaze();
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
		System.out.println("Game ended. " + winner.getName() + " win.");
		playerPoints.put(winner, playerPoints.get(winner) + POINTS_SUCCESS);

		System.out.println("Results: \n" + results());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		restart();
	}

	private String results() {
		StringBuilder b = new StringBuilder();

		for (Map.Entry<Player, Integer> e : playerPoints.entrySet()) {
			b.append(e.getKey().getName())
					.append(": ")
					.append(e.getValue())
					.append("\n");
		}

		return b.toString();
	}

	private Point2D.Float pointAfterCollisionCheck(Point2D.Float old, Point2D.Float newPoint, float playerRadius, int[][] maze) {

		if (newPoint.y > maze.length - playerRadius) {
			newPoint.y = maze.length - playerRadius;
		}

		ArrayList<Integer> collisionX = new ArrayList<Integer>();
		ArrayList<Integer> collisionY = new ArrayList<Integer>();

		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {

				if (maze[y][x] != 0 && maze[y][x] != 3) {
					if (collidesWithUnitSquare(newPoint, playerRadius, x, y)) {
						if (maze[y][x] == 1) {
							collisionX.add(x);
							collisionY.add(y);
						}
					}
				}
			}
		}

		float diffX = newPoint.x - old.x;
		float diffy = newPoint.y - old.y;

		float stepX = diffX * step;
		float stepY = diffy * step;

		float newX = old.x;
		float newY = old.y;

		if (collisionX.size() != 0) {
			//System.out.println(collisionX.size());
			boolean collisionDetected = false;
			for (float xx = old.x, yy = old.y; xx < newPoint.x; xx += stepX, yy += stepY) {

				for (int i = 0; i < collisionX.size(); i++) {

					if (collidesWithUnitSquare(new Point2D.Float(xx, yy), playerRadius, collisionX.get(i), collisionY.get(i))) {
						collisionDetected = true;
						break;
					}

				}

				if (collisionDetected) {
					break;
				} else {
					newX = xx;
					newY = yy;
				}

			}

			return new Point2D.Float(newX, newY);
		}

		return newPoint;
	}

	private boolean floatsAreEqual(float f1, float f2) {
		return Math.abs(f2 - f1) < eps;
	}

	private boolean pointsAreEqual(Point2D.Float p1, Point2D.Float p2) {
		return floatsAreEqual(p1.x, p2.x) && floatsAreEqual(p1.y, p2.y);
	}

	public void onPlayerMoved(Player player, Move move) {

		long time = System.currentTimeMillis();

		Move2D move2d = Move2D.of(move, scalex, scaley);
		Point2D.Float oldPos = playerPosition.get(player);
		Point2D.Float newPos = new Point2D.Float(oldPos.x + move2d.getX(), oldPos.y + move2d.getY());

		Point2D.Float afterCollisionCheck = pointAfterCollisionCheck(oldPos, newPos, playerRadius, maze);

		if (!pointsAreEqual(newPos, afterCollisionCheck)) {
			if (time - previousCollision.get(player) >= TIME_BETWEEN_COLLISIONS) {
				previousCollision.put(player, time);
				playerPoints.put(player, playerPoints.get(player) - POINTS_COLLISION);
			}
		}

		//CollisionCheck check = collides(newPos, playerRadius, maze);

		playerPosition.put(player, afterCollisionCheck);

//		if (check == CollisionCheck.NOTCOLLIDES || check == CollisionCheck.WIN) {
//			playerPosition.put(player, newPos);
//		} 

		if (afterCollisionCheck.y <= 1 - playerRadius) {
			gameEnded(player);
		}
	}

	private Point2D.Float startingPoint() {

		int x = 0, y = 0;

		boolean found = false;
		for (y = 0; y < maze.length; y++) {
			for (x = 0; x < maze[y].length; x++) {
				if (maze[y][x] == 3) {
					found = true;
					break;
				}
			}

			if (found) {
				break;
			}
		}

		float xf = x + 0.5f;
		float yf = y + 0.5f;

		Point2D.Float startingPoint = new Point2D.Float(xf, yf);
		System.out.println(startingPoint);

		return startingPoint;
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
		playerPoints.put(player, 0);
		previousCollision.put(player, 0l);
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
