import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

//https://hurna.io/academy/algorithms/maze_pathfinder/dijkstra.html
//used this site to learn about Dijkstra's algorithm
public class Dijkstra {

	public class Node {
		int i, j, type;
		int distance;
		ArrayList<Node> adj;
		boolean visited = false;
		Node parent = null;
		public Node(int i, int j, int type) {
			this.i = i;
			this.j = j;
			this.type = type;
			adj = new ArrayList<Node>();
		}

		public ArrayList<Node> getUnvisitedNeighbors(){
			ArrayList<Node> neigh = new ArrayList<Node>();
			for (Node node : adj)
				if (node.visited == false)
					neigh.add(node);
			return neigh;
		}
	}
	
	boolean pathFound;
	public long timeTaken;

	public Dijkstra() {
		pathFound = false;

		Cell[][] grid = GameMain.grid;
		//convert Cell objects into Nodes
		Node[][] nodes = new Node[grid.length][grid[0].length];
		Node start = null;
		ArrayList<Node> exits = new ArrayList<Node>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				nodes[i][j] = new Node(i, j, grid[i][j].getType());
				if (grid[i][j].getType() == Cell.START)
					start = nodes[i][j];
				if (grid[i][j].getType() == Cell.EXIT)
					exits.add(nodes[i][j]);
			}
		}

		//adding neighbor pointers to all nodes
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

		//set all distances except the starting node to infinity
		for (int i = 0; i < nodes.length; i++) 
			for (int j = 0; j < nodes[i].length; j++) 
				nodes[i][j].distance = Integer.MAX_VALUE;
		start.distance = 0;

		Comparator<Node> distanceComp = new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				return n1.distance - n2.distance;
			}
		};
		
		long time1 = System.currentTimeMillis(); 
		//try to find solutions for each of the exits
		ArrayList<Node> solutions = new ArrayList<Node>();
		for (Node exit : exits) 
			solutions.add(solve(start, exit, distanceComp));
		long time2 = System.currentTimeMillis();
		timeTaken = time2 - time1;
		
		ArrayList<Node> solutionsAux = new ArrayList<Node>();
		for (int i = 0; i < solutions.size(); i++) {
			Node node = solutions.get(i);
			solutionsAux.add(i, new Node(node.i, node.j, node.type));
			solutionsAux.get(i).parent = node.parent;
		}
		
		
		//find the length of each solution's path
		int[] pathLengths = new int[solutions.size()];
		for (int i = 0; i < pathLengths.length; i++)
			pathLengths[i] = 0;
		for (int i = 0; i < solutions.size(); i++) {
			Node exit = solutions.get(i);
			if (exit.parent != null) {
				while (exit.parent != null) {
					if (exit.type != Cell.START && exit.type != Cell.EXIT)
						pathLengths[i]++;
					exit = exit.parent;
				}
			}
		}
		
		//compare path lengths to find the shortest one
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for (int i = 0; i < pathLengths.length; i++) {
			if (pathLengths[i] < min) {
				min = pathLengths[i];
				minIndex = i;
			}
		}
		
		//if a solution is found, highlight the path on the GUI
		if (minIndex != -1) {
			pathFound = true;
			Node exit = solutionsAux.get(minIndex);
			while (exit.parent != null) {
				if (exit.type != Cell.START && exit.type != Cell.EXIT)
					GameMain.grid[exit.i][exit.j].setType(Cell.HIGHLIGHT);
				exit = exit.parent;
			}
			if (exit.type != Cell.START)
				pathFound = false;
		}
		else
			pathFound = false;
	}

	private Node solve(Node start, Node exit, Comparator<Node> distanceComp) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>(distanceComp);
		pq.add(start);

		Node currentNode = null;
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			currentNode.visited = true;
			for (Node neighbor : currentNode.getUnvisitedNeighbors()) {
				int minDistance = (int)(Math.min(neighbor.distance, currentNode.distance + 1));
				if (minDistance != neighbor.distance) {
					neighbor.distance = minDistance;
					neighbor.parent = currentNode;
					
					//this is for making sure neighbors priority is updated in pq
					//it might not be necessary
					if (pq.contains(neighbor)) {
						pq.remove(neighbor);
						pq.add(neighbor);
					}
					if (!pq.contains(neighbor)) 
						pq.add(neighbor);
				}
			}
		}
		
		return exit;
	}


	public boolean getPathFound() { return pathFound; }
}

