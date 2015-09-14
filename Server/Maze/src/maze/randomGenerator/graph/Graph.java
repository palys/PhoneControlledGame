package maze.randomGenerator.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

	private Set<Node> nodes;
	
	private Node start = null;
	
	private Node end = null;
	
	public Graph() {
		nodes = new HashSet<Node>();
	}
	
	public void setStartNode(Node n) {
		nodes.add(n);
		start = n;
	}
	
	public void setEndNode(Node n) {
		nodes.add(n);
		end = n;
	}
	
	public void addNode(Node n) {
		nodes.add(n);
	}
	
	public Node get(int x, int y) {
		for (Node n : nodes) {
			if (n.x == x && n.y == y) {
				return n;
			}
		}
		return null;
	}
	
	private Map<Node, Boolean> notVistedMap() {
		Map<Node, Boolean> map  = new HashMap<Node, Boolean>();
		
		for (Node n : nodes) {
			map.put(n, false);
		}
		
		return map;
	}
	
	public boolean isTraversableFromStartToEnd() {
		
		if (start == null || end == null) {
			return false;
		}
		
		Map<Node, Boolean> visited = notVistedMap();
		
		return traverse(start, visited);
	}
	
	private boolean traverse(Node n, Map<Node, Boolean> visited) {
		
		visited.put(n, true);
		
		if (n.equals(end)) {
			return true;
		}
		
		if (n.getState() == 1) {
			return false;
		}
		
		for (Node neighbour : n.getNeighbours()) {
			
			if (!visited.get(neighbour)) {
				if (traverse(neighbour, visited)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
