package maze;

import java.awt.Point;


public class RandomMazeGanarator implements MazeGenerator {
	
	private int height;
	
	private int width;
	
	private Point start;
	
	private Point end;

	public RandomMazeGanarator(int height, int width, Point start, Point end) {
		super();
		this.height = height;
		this.width = width;
		this.start = start;
		this.end = end;
	}

	@Override
	public int[][] generateMaze() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
