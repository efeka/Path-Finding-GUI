import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {

	public class Node implements Comparable {

		Node parent;
		ArrayList<Node> neighbors;
		int i, j, type;

		public Node(int i, int j, int type) {
			neighbors = new ArrayList<Node>();
			parent = null;
			this.i = i;
			this.j = j;
			this.type = type;
		}

		//F = G + H
		double G; //path from start to this node
		double H; //shortest distance between this and exit

		public double findF(Node start, Node exit) {
			return findG(start) + findHeuristicDistance(exit);
		}
		
		//diagonal movement costs 1.5, the rest costs 1
		public double findG(Node node) {
			int diff1 = (int)(Math.abs(i - node.i));
			int diff2 = (int)(Math.abs(j - node.j));
			if (diff1 == diff2)
				return diff1 * 1.5;
			return diff1 + diff2;
		}
		
		public double findHeuristicDistance(Node node) {
			int x1 = i, y1 = j;
			int x2 = node.i, y2 = node.j;
			double diffX = (x1 - x2) * (x1 - x2);
			double diffY = (y1 - y2) * (y1 - y2);
			return Math.sqrt(diffX + diffY);
		}

		public int compareTo(Object node) {
			double F1 = findF(start, exit);
			double F2 = ((Node)node).findF(start, exit);
			return (int) (F1 - F2);
		}

	}
	
	Node start, exit;
	private boolean pathFound = false;
	public long timeTaken;

	public AStar() {
		Cell[][] grid = GameMain.grid;
		Node[][] nodes = new Node[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++) 
			for (int j = 0; j < grid[i].length; j++) 
				nodes[i][j] = new Node(i, j, grid[i][j].getType());

		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] != null) {
					for (int k = -1; k < 2; k++) {
						for (int l = -1; l < 2; l++) {
							//try catch is to avoid checking all the edge cases
							try {
								if (nodes[i + k][l + j].type != Cell.BARRIER) {
									nodes[i][j].neighbors.add(nodes[i + k][l + j]);
								}
							}
							catch(ArrayIndexOutOfBoundsException e) {
								continue;
							}
						}
					}
					nodes[i][j].neighbors.remove(nodes[i][j]);
				}
			}
		}
		
		start = null;
		ArrayList<Node> exits = new ArrayList<Node>();
		for (int i = 0; i < grid.length; i++) { 
			for (int j = 0; j < grid[i].length; j++) {
				if (nodes[i][j].type == Cell.START)
					start = nodes[i][j];
				else if (nodes[i][j].type == Cell.EXIT) 
					exits.add(nodes[i][j]);
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
		
		if (minPath == null)
			pathFound = false;
		else
			pathFound = true;
		if (pathFound)
			highlightSolution(minPath);
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
	
	public boolean getPathFound() {
		return pathFound;
	}

	public List findPath(Node start, Node exit) {
		Comparator<Node> comparatorF = new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				return n1.compareTo(n2);
			}
		};

		PriorityQueue open = new PriorityQueue(comparatorF);
		LinkedList closed = new LinkedList();
		
		start.G = 0;
		start.H = start.findHeuristicDistance(exit);
		start.parent = null;
		open.add(start);

		while(!open.isEmpty()) {
			Node node = (Node)open.remove();
			if (node == exit) 
				return getPath(exit);
			
			ArrayList<Node> neighbors = node.neighbors;
			for (int i = 0; i < neighbors.size(); i++) {
				Node adj = neighbors.get(i);
				boolean isOpen = open.contains(adj);
				boolean isClosed = closed.contains(adj);
				double G = node.findG(start) + node.findG(adj);
				
				if ((!isOpen && !isClosed) || G < adj.findG(node)) {
					adj.parent = node;
					adj.G = G;
					adj.H = adj.findHeuristicDistance(exit);
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
		return list;
	}

}

