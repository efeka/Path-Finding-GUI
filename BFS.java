import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;


public class BFS {
	
	public class Node implements Comparable {

		Node parent;
		ArrayList<Node> adj;
		int i, j, type;
		double heuristic;

		public Node(int i, int j, int type) {
			adj = new ArrayList<Node>();
			parent = null;
			this.i = i;
			this.j = j;
			this.type = type;
		}
		
		public double findHeuristicDistance(Node exit) {
			int x1 = i, y1 = j;
			int x2 = exit.i, y2 = exit.j;
			double diffX = (x1 - x2) * (x1 - x2);
			double diffY = (y1 - y2) * (y1 - y2);
			return heuristic = Math.sqrt(diffX + diffY);
		}

		public int compareTo(Object node) {
			double h1 = findHeuristicDistance(exit);
			double h2 = ((Node)node).findHeuristicDistance(exit);
			return (int) (h1 - h2);
		}
	}
	
	Node start, exit;
	boolean pathFound;
	public long timeTaken;
	
	public BFS() {
		Cell[][] grid = GameMain.grid;
		Node[][] nodes = new Node[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) 
			for (int j = 0; j < grid[i].length; j++) 
				nodes[i][j] = new Node(i, j, grid[i][j].getType());
		
		for (int i = 0; i < nodes.length; i++)  {
			for (int j = 0; j < nodes[i].length; j++) {
				Node node = nodes[i][j];
				//up
				try {
					if (nodes[i - 1][j].type != 1)
						node.adj.add(nodes[i - 1][j]);
				} catch(Exception e) {}
				//down
				try {
					if (nodes[i + 1][j].type != 1)
						node.adj.add(nodes[i + 1][j]);
				} catch(Exception e) {}
				//left
				try {
					if (nodes[i][j - 1].type != 1)
						node.adj.add(nodes[i][j - 1]);
				} catch(Exception e) {}
				//right
				try {
					if (nodes[i][j + 1].type != 1)
						node.adj.add(nodes[i][j + 1]);
				} catch(Exception e) {}

			}
		}
		
		start = null;
		ArrayList<Node> exits = new ArrayList<Node>();
		for (int i = 0; i < grid.length; i++) { 
			for (int j = 0; j < grid[i].length; j++) {
				if (nodes[i][j].type == Cell.START)
					start = nodes[i][j];
				else if (nodes[i][j].type == Cell.EXIT) {
					nodes[i][j].findHeuristicDistance(nodes[i][j]);
					exits.add(nodes[i][j]);
				}
			}
		}
		
		long time1 = System.currentTimeMillis();
		List[] paths = new List[exits.size()];
		for (int i = 0; i < paths.length; i++) {
			exit = exits.get(i);
			List list = findPath(start, exit);
			paths[i] = list;
		}
		long time2 = System.currentTimeMillis();
		timeTaken = time2 - time1;
		
		List minPath = paths[0];
		for (int i = 0; i < paths.length; i++) {
			if (minPath == null || paths[i] == null)
				continue;
			if (paths[i].size() != 0 && paths[i].size() < minPath.size())
				minPath = paths[i];
		}
		
		if (minPath != null)
			if (pathFound)
				highlightSolution(minPath);
		
	}
	
	public List findPath(Node start, Node exit) {
		Comparator<Node> comparatorF = new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				return n1.compareTo(n2);
			}
		};

		PriorityQueue open = new PriorityQueue(comparatorF);
		LinkedList closed = new LinkedList();
		
		start.heuristic = start.findHeuristicDistance(exit);
		start.parent = null;
		open.add(start);

		while(!open.isEmpty()) {
			Node node = (Node)open.remove();
			if (node == exit) 
				return getPath(exit);
			
			ArrayList<Node> neighbors = node.adj;
			for (int i = 0; i < neighbors.size(); i++) {
				Node adj = neighbors.get(i);
				boolean isOpen = open.contains(adj);
				boolean isClosed = closed.contains(adj);
				
				if ((!isOpen && !isClosed) || node.heuristic > adj.heuristic) {
					adj.parent = node;
					if (isClosed)
						closed.remove(adj);
					if (!isOpen)
						open.add(adj);
				}
			}
			closed.add(node);	
		}
		return null;
	}

	public List getPath(Node exit) {
		LinkedList list = new LinkedList();
		ArrayList<Node> visited = new ArrayList<Node>();
		while (exit.parent != null) {
			if (visited.contains(exit)) {
				pathFound = false;
				return null;
			}
			list.add(exit);
			visited.add(exit);
			exit = exit.parent;
		}
		pathFound = true;
		return list;
	}

	
	public boolean getPathFound() {
		return pathFound;
	}
	
	private void highlightSolution(List path) {
		for (Object n : path) {
			Node node = ((Node)n);
			if (node.type == Cell.EXIT)
				continue;
			int i = node.i;
			int j = node.j;
			GameMain.grid[i][j].setType(Cell.HIGHLIGHT);
		}
	}
}
