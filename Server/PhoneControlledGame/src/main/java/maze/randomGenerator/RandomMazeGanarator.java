package maze.randomGenerator;

import java.awt.Point;
import java.util.Random;

import maze.MazeGenerator;
import maze.randomGenerator.graph.Graph;
import maze.randomGenerator.graph.Node;


public class RandomMazeGanarator implements MazeGenerator {
	
	private Random random;
	
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
		this.random = new Random();
	}

	@Override
	public int[][] generateMaze() {
		
		int[][] maze;
		if (primsMazeIsPossible()) {
			
			maze = new PrimsMazeGenerator(height, width, start, end).generateMaze();
		} else {
			
			maze = generateBasicMaze();
		}
		
		return maze;
	}
	
	private boolean primsMazeIsPossible() {
		return (height % 2 == 1) &&
				(width % 2 == 1) &&
				(start.x == width / 2) &&
				(end.x == width / 2) &&
				(start.y == height - 1) &&
				(end.y == 0);
	}
	
	private int[][] generateBasicMaze() {
		
		int[][] maze = fullMaze();
		setStart(maze, start);
		setEnd(maze, end);
		
		Graph g = generateGraph(maze);
		
		makeTraversable(maze, g);
		
		return maze;
	}
	
	private void makeTraversable(int[][] maze, Graph g) {
		while(!g.isTraversableFromStartToEnd()) {
			int x = 1 + random.nextInt(width - 2);
			int y = 1 + random.nextInt(height - 2);
			
			Node n = g.get(x,  y);
			
			if (n == null) {
				
			} else if (maze[y][x] == 1) {
				n.setState(0);
				maze[y][x] = 0;
			}
		}
	}
	
	private void setEnd(int[][] maze, Point endPoint) {
		maze[endPoint.y][endPoint.x] = 2;
	}
	
	private void setStart(int[][] maze, Point startPoint) {
		maze[startPoint.y][startPoint.x] = 3;
	}
	
	private Graph generateGraph(int[][] maze) {
		
		Node[][] nodes = new Node[height][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				nodes[j][i] = new Node(i, j, maze[j][i]);
			}
		}
		
		Graph g = new Graph();
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				
				Node n = nodes[j][i];
				if (i > 0) {
					n.addNeighbour(nodes[j][i-1]);
				}
				if (i < width - 1) {
					n.addNeighbour(nodes[j][i+1]);
				}
				if (j > 0) {
					n.addNeighbour(nodes[j-1][i]);
				}
				if (j < height - 1) {
					n.addNeighbour(nodes[j+1][i]);
				}
				
				if (n.getState() == 2) {
					g.setEndNode(n);
				} else if (n.getState() == 3) {
					g.setStartNode(n);
				} else {
					g.addNode(n);
				}
				
				
			}
		}
		
		return g;
	}
	
	private int[][] fullMaze() {
		
		int[][] maze = new int[height][width];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				maze[j][i] = 1;
			}
		}
		
		return maze;
	}
	
	

}
