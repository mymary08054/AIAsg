package edu;

public class Node implements Comparable<Node>{
	
	/**
	 * xy coords of node.
	 */
	private int[] coords;
	
	/**
	 * 4x2 array. 4 possible neighbors registered by coordinate number.
	 */
	private int[][] neighbors;
	/**
	 * number of neighbors
	 */
	private int numNeighbors;
	/**
	 * value refers to the corresponding values per symbol
	 *  case '.':
		   map[y][x] = 1;
	   case ',':
		   map[y][x] = 2;
	   case '#':
		   map[y][x] = -1;
	   case 's':
		   map[y][x] = 3;
	   case 'g':
		   map[y][x] = 0;
	 */
	private int value;
	
	private boolean visited;
	
	private Node parent;
	/**
	 * Cost to reach this node.
	 */
	private int cost;
	/**
	 * f = g + h.
	 * g = distance to this node from start.
	 * h = heuristic cost (Manhattan with max).
	 */
	private int f;
	
	/**
	 * Node representation of the map.
	 * @param nValue determines what symbol it is (comma, period, etc)
	 * @param y row number (up/down)
	 * @param x column number (left/right)
	 */
	public Node(int nValue, int y, int x){
		this.value = nValue;
		this.coords = new int[2];
		this.coords[0] = y;
		this.coords[1] = x;
		neighbors = new int[4][2];
		this.visited = false;
		this.parent = null;
		this.cost = 0;
		this.f = 0;
		numNeighbors = 0;
	}
	
	/**
	 * returns pos if n.f>this.f; 0 if n.f=this.f; neg if n.f<this.f.
	 * Used for priority queue.
	 */
	public int compareTo(Node n) {
        return this.f - n.f;
    }
	
	/**
	 * get f.
	 * @return f = h+g
	 */
	public int getF() {
		return f;
	}

	/**
	 * set f value.
	 * @param f = h+g
	 */
	public void setF(int f) {
		this.f = f;
	}

	/**
	 * get cost of getting to this node.
	 * @return cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * set the cost.
	 * @param cost new cost value
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * get parent node.
	 * @return
	 */
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean equals(int y, int x){
		if(y==this.getCoords(0) && x==this.getCoords(1))
			return true;
		else
			return false;
	}
	
	/**
	 * return neighbors.
	 * @param n neighbor number
	 * @param index neighbor xy (1,0)
	 * @return neighbor
	 */
	public int[] getNeighbor(int n) {
		return neighbors[n];
	}
	
	/**
	 * return neighbors.
	 * @param n neighbor number
	 * @param index neighbor xy (1,0)
	 * @return neighbor
	 */
	public int getNeighbor(int n, int index) {
		return neighbors[n][index];
	}
	
//	public int getNeighbors() {
//		return neighbors[n][index];
//	}
//	
	/**
	 * given neighbor coords,
	 * set neighbor in 4x2 array.
	 * @param x row index
	 * @param y column index
	 * @return neighbor
	 */
	public void setNeighbors(int y, int x) {
		this.neighbors[numNeighbors][0] = y;
		this.neighbors[numNeighbors][1] = x;
		this.numNeighbors++;
	}

	/**
	 * returns y coords if index = 0; x if index = 1.
	 * @param index y,x
	 * @return coord
	 */
	public int getCoords(int index) {
		return coords[index];
	}
	
	/**
	 * return coords as size 2 array.
	 * @return coords
	 */
	public int[] getCoords() {
		return coords;
	}
	
	/**
	 * get value.
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * set value
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * get number of neighbors to this node.
	 * @return num neighbors
	 */
	public int getNumNeighbors() {
		return numNeighbors;
	}

	/**
	 * set num neighbors.
	 * @param numNeighbors
	 */
	public void setNumNeighbors(int numNeighbors) {
		this.numNeighbors = numNeighbors;
	}
	
	/**
	 * toString of Node's coordinates.
	 */
	public String toString(){
		String str = "";
		str +=  " [y: " + this.getCoords(0) + " x: " + this.getCoords(1);
	    str += "]";
	    return str;
	}
	
}
