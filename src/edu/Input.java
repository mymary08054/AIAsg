package edu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class Input
{
	/**
	 * node representation of given map.
	 * Each node keeps track of its own value and neighbors.
	 */
	public static Node[][] map = null;
	/**
	 * coordinates of start node
	 */
	public static int[] start = null;
	/**
	 * coordinates of goal node
	 */
	public static int[] goal = null;

	/**
	 * number of commas in map. Used for heuristic.
	 */
	public static int numCommas;
	/**
	 * number of periods in map. Used for heuristic.
	 */
	public static int numPeriods;
	/**
	 * number of nodes expanded.
	 */
	public static int expNodes;
	
	/**
	 * user chosen path. 
	 */
	public static String pathChoice;
	
	/**
	 * start value (for nodes).
	 */
	public static final int START_ID = 3;
	/**
	 * goal value (for nodes).
	 */
	public static final int GOAL_ID = 0;

	/**
	 * main program will prompt user for a map and a search
	 * function. Program will run search function on map and
	 * output the results. Each run allows only one search.
	 * @param args
	 * @throws java.io.IOException
	 */
   public static void main (String [] args) throws java.io.IOException
   {
	   readInput();
	   if(pathChoice.equals("0")) {
		   System.out.println("Running BFS...");
		   BFSpath();
	   } else if(pathChoice.equals("1")) {
		   System.out.println("Running DFS...");
		   DFSpath();
	   } else if(pathChoice.equals("2")) {
		   System.out.println("Running A*...");
		   Apath();
	   } else {
		   System.out.println("Please input 0 for BFS, 1 for DFS, 2 for A*");
	   }
	   
  }
   
   /**
    * if goal found, print cost, path and number of nodes.
    * If not found, print cost as infinite and number of nodes expanded.
    * @return string results.
    */
   private static String printResults(int i) {
       Node curr = lookUpMap(goal);
       String str = "";
       String path = "";
       if(i == 0){
    	   str = "Goal found!";
    	   
    	   str += "\nGoal Path: \n";
    	   while (curr != null) {
               path += curr.toString() + "\n";
               curr = curr.getParent();
           }
           str += path;
           str += "\nCost: " + lookUpMap(goal).getCost();
       } else {
    	   str = "Cannot find path to goal.";
    	   str += "\nCost: Infinite";
       }
       str+= "\nNumber of nodes expanded: " + expNodes;
       return str;
   }
   
   /**
    * BFS search on map.
    */
   private static void BFSpath(){
	   //queue: first = head, last = tail
	   //add to head, remove from tail
	   Queue<Node> queue = new LinkedList<Node>();
	   LinkedList<Node> neighbors = new LinkedList<Node>();
	   //current node we're looking at
	   Node curr = null;
	   int y,x;
	   
	   //load start node
	   queue.offer(lookUpMap(start));
	   lookUpMap(start).setVisited(true);
	   //while queue is not empty...BFS
	   while(queue.size() != 0){
		   curr = queue.remove();
		   y = curr.getCoords(0);
		   x = curr.getCoords(1);
		   if(curr.getValue() == GOAL_ID){
			   //found goal!
			   System.out.println(printResults(0));
			   return;
		   }
		   //else, continue BFS
		   expNodes++;
		   //add neighbors of curr to queue
		   neighbors = getNeighbors(curr.getCoords(0), curr.getCoords(1), true);
		   for(Node n: neighbors){
			   //if(curr.getParent() != null && !curr.getParent().equals(n))
			   n.setParent(curr);
			   n.setCost(curr.getCost() + n.getValue());
			   n.setVisited(true);
			   queue.offer(n);
		   }
	   }
	   //can't find path
	   System.out.println(printResults(1));
   }
   
   /**
    * DFS search on map.
    */
   private static void DFSpath(){
	   Stack<Node> stack = new Stack<Node>();
	   Node curr = null;
	   LinkedList<Node> neighbors = new LinkedList<Node>();
	   int y,x;
	   
	   stack.push(lookUpMap(start));
	   lookUpMap(start).setVisited(true);
	   expNodes = 0;
	   while(stack.size() != 0){
		   curr = stack.pop();
		   y = curr.getCoords(0);
		   x = curr.getCoords(1);
		   if(curr.getValue() == GOAL_ID){
			   //found goal!
			   System.out.println(printResults(0));
			   return;
		   }
		   //else continue DFS
		   expNodes++;
		   //add neighbors of curr to queue
		   neighbors = getNeighbors(curr.getCoords(0), curr.getCoords(1), true);
		   for(Node n: neighbors){
			   n.setParent(curr);
			   n.setCost(curr.getCost() + n.getValue());
			   n.setVisited(true);
			   stack.push(n);
		   }
	   }
	 //can't find path
	   System.out.println(printResults(1));
   }
   
   /**
    * A* search path on map.
    */
   private static void Apath() {
	   PriorityQueue<Node> pqueue = new PriorityQueue<Node>();
	   Node curr = null;
	   LinkedList<Node> neighbors = new LinkedList<Node>();
	   int y,x,c; //c is cost of current node
	   
	   Node startnode = lookUpMap(start);
	   pqueue.offer(startnode);
	   startnode.setVisited(true);
	   startnode.setF(Aheuristic(start));
	   expNodes = 0;
	   while(pqueue.size() != 0){
		   curr = pqueue.poll();
		   y = curr.getCoords(0);
		   x = curr.getCoords(1);
		   if(curr.getValue() == GOAL_ID){
			   //found goal!
			   System.out.println(printResults(0));
			   return;
		   }
		   //else continue A*
		   expNodes++;
		   //add neighbors of curr to queue
		   neighbors = getNeighbors(curr.getCoords(0), curr.getCoords(1), true);
		   for(Node n: neighbors){
			   c = curr.getCost() + n.getValue();
			   if (!n.isVisited() || n.getCost() > c) {
				   n.setParent(curr);
				   n.setCost(c);
				   n.setF(c + Aheuristic(n.getCoords()));
				   if(n.isVisited())
					   pqueue.remove(n);
				   n.setVisited(true);
				   pqueue.offer(n);
			   }
		   }
	   }
	 //can't find path
	   System.out.println(printResults(1));
   }
   
   /**
    * calculates heuristic path cost from current node to goal.
    * Using Manhattan distance, assuming path has all
    * commas, then periods if commas run out. If periods
    * run out, too -> cannot reach goal.
    * @param n current node
    * @return pathcost (h)
    */
   private static int Aheuristic(int[] n){
	   int mDistance = Math.abs(n[0]-goal[0]);
	   mDistance += Math.abs(n[1]-goal[1]);
	   if(mDistance >= numPeriods)
		   return mDistance;
	   else if((mDistance - numPeriods - 1) >= numCommas)
		   return mDistance + 1 + (mDistance - numPeriods - 1)*2;
	   else {
		   return -1;
	   }
   }
   
   /**
    * given info, create the new node with correct
    * value and coordinates.
    * @param symbol character found in map (comma, pound, etc)
    * @param x column number (left/right)
    * @param y row number (up/down)
    */
   private static void parseMapSymbol(char symbol, int x, int y){
	   switch(symbol){
	   case '.':
		   map[y][x] = new Node(1,y,x);
		   numPeriods++;
		   break;
	   case ',':
		   map[y][x] = new Node(2,y,x);
		   numCommas++;
		   break;
	   case 's':
		   map[y][x] = new Node(START_ID,y,x);
		   start[0] = y;
		   start[1] = x;
		   break;
	   case 'g':
		   map[y][x] = new Node(GOAL_ID,y,x);
		   goal[0] = y;
		   goal[1] = x;
		   break;
	   case '#':
		   map[y][x] = new Node(-1,y,x);
		   return;
	   default:
		   System.out.println(symbol);
		   System.err.println("Invalid symbol input");
	   }
	   if(x != 0){
		   if(map[y][x-1].getValue() != -1){ //is not a #
			   //update adj list
			   map[y][x].setNeighbors(y, x-1);
			   map[y][x-1].setNeighbors(y,x);
		   }
	   }
	   if(y != 0){
		   if(map[y-1][x].getValue() != -1){ //is not a #
			   //update adj list
			   map[y][x].setNeighbors(y-1, x);
			   map[y-1][x].setNeighbors(y,x);
		   }
	   }
   }
   
   /**
    * Look up node in map adjacency list by coord.
    * @param coord size 2 array.
    * @return node in given coord in map
    */
   private static Node lookUpMap(int[] coord){
	   return map[coord[0]][coord[1]];
   }
   
   /**
    * get the neighbors in each of the 4 cardinal directions
    * of the given coordinate.
    * @param x column number (left/right)
    * @param y row number (up/down)
    * @param v true/false
    * @return linkedlist of neighbors
    */
   private static LinkedList<Node> getNeighbors(int x, int y, boolean v) {
       LinkedList<Node> list = new LinkedList<Node>();
       if (inBound(x-1, y) && (!map[x-1][y].isVisited() || !v) ) {
          list.add(map[x-1][y]);
       }
       if (inBound(x, y-1) && (!map[x][y-1].isVisited() || !v)) {
          list.add(map[x][y-1]);
       }
       if (inBound(x+1, y) && (!map[x+1][y].isVisited() || !v)) {
          list.add(map[x+1][y]);
       }
       if (inBound(x, y+1) && (!map[x][y+1].isVisited() || !v)) {
          list.add(map[x][y+1]);
       }
      return list; 
   }
   
   /**
    * helper function to ensure that the coordinates are within
    * the map.
    * @param y row number (up/down)
    * @param x column number (left/right)
    * @return true if in, false if not
    */
   private static boolean inBound(int y, int x) {
       return y >= 0 && x >= 0 && y < map.length 
       && x < map[0].length && (map[y][x].getValue() != -1);
   }
   
   /**
    * reads user inputs and sets the global variables as necessary.
    * @throws FileNotFoundException
    */
   private static void readInput() throws FileNotFoundException{
	  numCommas = 0;
	  numPeriods = 0;
	  pathChoice = "";
	  expNodes = 0;
      start = new int[2]; 
	  goal = new int[2];
      String ch;
      Scanner inputScanner = new Scanner (System.in);
      System.out.println("Enter map file name: ");
      ch = inputScanner.nextLine();
      System.out.println("Enter 0 for BFS, 1 for DFS, 2 for A*: ");
      pathChoice = inputScanner.nextLine();
      FileInputStream fis = new FileInputStream("../data/"+ch);
      Scanner fileScanner = new Scanner(fis);

      //load map dimensions & create map array
      if(fileScanner.hasNextLine()){
    	  String dimensions = fileScanner.nextLine();
    	  String[] elements = dimensions.split(" ");
    	  if(elements.length != 2){
    		  System.err.println("Error: " + ch + "'s dimension format is incorrect.");
    	  }
    	  //get rid of border #s
    	  //so we subtract 2 on x,y to account for that
    	  map = new Node[Integer.parseInt(elements[1]) - 2][Integer.parseInt(elements[0]) - 2];
    	  //forget first mapline of #'s
    	  if(fileScanner.hasNextLine()){
    		  fileScanner.nextLine();
    	  }
      }
      
      //load map array
      //map[column][row]
      //map[up/down][left/right]
      String curr = ""; //current line
      int x = 0,y = 0;
      while(fileScanner.hasNextLine() && y != map.length){
    	  curr = fileScanner.nextLine();
    	  char[] charLine = curr.toCharArray();
    	  //skip 1st # and last # rows
    	  for(int i = 1; i <= map[0].length; i++){
    		   parseMapSymbol(charLine[i], x, y);
    		  x++;
    	  }
    	  x = 0;
    	  y++;
      }
      
      fileScanner.close();
   }
   
}
