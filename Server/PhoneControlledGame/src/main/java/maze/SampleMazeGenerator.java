package maze;

public class SampleMazeGenerator implements MazeGenerator {

	@Override
	public int[][] generateMaze() {
		
		int [][] maze = new int[][]{
				{0, 0, 0, 1, 2, 1, 0, 0, 0},
				{0, 1, 1, 1, 0, 1, 0, 0, 0},
				{0, 1, 0, 0, 0, 1, 1, 1, 1},
				{1, 1, 0, 1, 0, 0, 0, 0, 1},
				{1, 0, 0, 1, 1, 1, 1, 0, 1},
				{1, 0, 1, 1, 0, 0, 0, 0, 1},
				{1, 0, 0, 0, 0, 1, 1, 1, 1},
				{1, 1, 1, 1, 3, 1, 1, 1, 0}
		};
		
		return maze;
	}

}
