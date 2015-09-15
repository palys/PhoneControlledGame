package maze.randomGenerator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import maze.MazeGenerator;

public class PrimsMazeGenerator implements MazeGenerator {
	
	private class WallHolder {
		public Point wall;
		
		public Point opositeCell;

		public WallHolder(Point wall, Point opositeCell) {
			super();
			this.wall = wall;
			this.opositeCell = opositeCell;
		}
		
	}
	
	private Random random;
	
	private int height;
	
	private int width;
	
	private Point start;
	
	private Point end;

	public PrimsMazeGenerator(int height, int width, Point start, Point end) {
		
		super();
		this.height = height;
		this.width = width;
		this.start = start;
		this.end = end;
		this.random = new Random();
	}

	@Override
	public int[][] generateMaze() {

		int[][] maze = initialMaze();
		
		ArrayList<WallHolder> walls = new ArrayList<WallHolder>();
		ArrayList<Point> cells = new ArrayList<Point>();
		
		Point firstPoint = new Point(start.x, start.y - 1);
		cells.add(firstPoint);
		walls.addAll(allWallsOfCellExceptOne(firstPoint, start));
		
		while (!walls.isEmpty()) {
			WallHolder wall = walls.get(random.nextInt(walls.size()));
			
			if (!cells.contains(wall.opositeCell)) {
				maze[wall.wall.y][wall.wall.x] = 0;
				List<WallHolder> wallsToAdd = allWallsOfCellExceptOne(wall.opositeCell, wall.wall);
				cells.add(wall.opositeCell);
				//System.out.println("Added " + wallsToAdd.size() + " walls");
				walls.addAll(wallsToAdd);
			}
			
			walls.remove(wall);
		}
		
		
		for (Point cell : cells) {
			maze[cell.y][cell.x] = 0;
		}
		
//		for (WallHolder h : walls) {
//			maze[h.wall.y][h.wall.x] = 1;
//		}
		
		return maze;
	}
	
	private List<WallHolder> allWallsOfCellExceptOne(Point cell, final Point one) {
		
		ArrayList<WallHolder> walls = new ArrayList<WallHolder>();
		
		walls.add(new WallHolder(new Point(cell.x, cell.y - 1), new Point(cell.x, cell.y - 2)));
		walls.add(new WallHolder(new Point(cell.x, cell.y + 1), new Point(cell.x, cell.y + 2)));
		walls.add(new WallHolder(new Point(cell.x + 1, cell.y), new Point(cell.x + 2, cell.y)));
		walls.add(new WallHolder(new Point(cell.x - 1, cell.y), new Point(cell.x - 2, cell.y)));
		
		Iterable<WallHolder> wallsIterable = Iterables.filter(walls, new Predicate<WallHolder>() {

			@Override
			public boolean apply(WallHolder h) {
				
				return isValidWall(h.wall) && !one.equals(h.wall);
			}
			
		});
		
		return Lists.newArrayList(wallsIterable);
		
	}
	
	private boolean isValidWall(Point p) {
		return p.x != 0 && p.x != width - 1 && p.y != 0 && p.y != height - 1;
	}
	
	private int[][] initialMaze() {
		int[][] maze = new int[height][width];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				maze[i][j] = 1;
			}
		}
		
		maze[start.y][start.x] = 3;
		maze[end.y][end.x] = 2;
		
		return maze;
	}

}
