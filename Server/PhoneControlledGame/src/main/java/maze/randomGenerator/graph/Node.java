package maze.randomGenerator.graph;

import java.util.HashSet;
import java.util.Set;

public class Node {

	public final int x;
	
	public final int y;
	
	private int state;
	
	private Set<Node> neighbours;
	
	public Node(int x, int y, int state) {
		this.x = x;
		this.y = y;
		this.neighbours = new HashSet<Node>();
		this.state = state;
	}
	
	public void addNeighbour(Node n) {
		this.neighbours.add(n);
	}
	
	public void removeNeighbour(Node n) {
		this.neighbours.remove(n);
	}
	
	public Set<Node> getNeighbours() {
		return neighbours;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public boolean isFree() {
		return state == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [x=" + x + ", y=" + y + ", state=" + state + "]";
	}
	
	
}
