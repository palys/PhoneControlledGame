package maze.randomGenerator;

import java.awt.Point;

import maze.MazeGenerator;

import com.google.common.collect.Range;

public class RandomMazeGeneratorBuilder {
	
	private int width = 2;
	
	private int height = 2;
	
	private Point start = new Point(0, 1);
	
	private Point end = new Point(1, 0);
	
	public RandomMazeGeneratorBuilder width(int newWidth) {
		
		width = newWidth;
		return this;
	}
	
	public RandomMazeGeneratorBuilder height(int newHeight) {
		height = newHeight;
		return this;
	}
	
	public RandomMazeGeneratorBuilder startPoint(Point newStartPoint) {
		start = newStartPoint;
		return this;
	}
	
	public RandomMazeGeneratorBuilder endPoint(Point newEndPoint) {
		end = newEndPoint;
		return this;
	}
	
	private void checkArgs() {
		
		if (width < 2) {
			throw new IllegalStateException("Width must be at least 2");
		}
		
		if (height < 2) {
			throw new IllegalStateException("Height must be at least 2");
		}
		
		Range<Integer> vertRange = Range.closedOpen(0, height);
		Range<Integer> horRange = Range.closedOpen(0, width);
		
		if (!rectContainPoint(vertRange, horRange, start)) {
			throw new IllegalStateException("Rectangle does not contain start point");
		}
		
		if (!rectContainPoint(vertRange, horRange, end)) {
			throw new IllegalStateException("Rectangle does not contain end point");
		}
	}
	
	private boolean rectContainPoint(Range<Integer> vertRange, Range<Integer> horRange, Point point) {
		return vertRange.contains(point.y) && horRange.contains(point.x);
	}

	public MazeGenerator build() {
		
		checkArgs();
		
		return new RandomMazeGanarator(height, width, start, end);
		
	}
}
