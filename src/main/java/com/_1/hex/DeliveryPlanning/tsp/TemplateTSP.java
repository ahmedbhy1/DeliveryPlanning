package com._1.hex.DeliveryPlanning.tsp;

import java.util.*;

public abstract class TemplateTSP implements TSP {
	private Integer[] bestSol;
	protected Graph g;
	private int bestSolCost;
	private int timeLimit;
	private long startTime;
	
	public void searchSolution(int timeLimit, Graph g){
		if (timeLimit <= 0) return;
		startTime = System.currentTimeMillis();	
		this.timeLimit = timeLimit;
		this.g = g;
		bestSol = new Integer[g.getNbVertices()];
		Collection<Integer> unvisited = new ArrayList<Integer>(g.getNbVertices()-1);
		for (int i=1; i<g.getNbVertices(); i++) unvisited.add(i);
		Collection<Integer> visited = new ArrayList<Integer>(g.getNbVertices());
		visited.add(0); // The first visited vertex is 0
		bestSolCost = Integer.MAX_VALUE;
		Map<Integer,Integer> costBetweenTakeUpAndReturn = new HashMap<>();
		costBetweenTakeUpAndReturn.put(0,0);
		Integer max_cost = 4;
		if (branchAndBound(0, unvisited, visited, 0,costBetweenTakeUpAndReturn,max_cost)){
			System.out.println("Solution found");
		}
		else {
			System.out.println("cost exceeded");
		}

	}


	public Integer getSolution(int i){
		if (g != null && i>=0 && i<g.getNbVertices())
			return bestSol[i];
		return -1;
	}
	
	public int getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract int bound(Integer currentVertex, Collection<Integer> unvisited);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g);
	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private boolean branchAndBound(int currentVertex, Collection<Integer> unvisited,
			Collection<Integer> visited, int currentCost, Map<Integer,Integer> costBetweenTakeUpAndReturn,Integer maximum_cost){

		costBetweenTakeUpAndReturn.put(currentVertex,currentCost);
		if (true || currentCost-costBetweenTakeUpAndReturn.get(this.g.getPredecessor(currentVertex))>maximum_cost){
			return false;
		}

		if (System.currentTimeMillis() - startTime > timeLimit) return false;
	    if (unvisited.size() == 0){ 
	    	if (g.isArc(currentVertex,0)){ 
	    		if (currentCost+g.getCost(currentVertex,0) < bestSolCost){ 
	    			visited.toArray(bestSol);
	    			bestSolCost = currentCost+g.getCost(currentVertex,0);
					return true;
	    		}
	    	}
	    } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
	        Iterator<Integer> it = iterator(currentVertex, unvisited, g);
	        boolean test = false;
			while (it.hasNext()){
	        	Integer nextVertex = it.next();
	        	visited.add(nextVertex);
	            unvisited.remove(nextVertex);
				//if(g.getPredecessors(nextVertex)){}
				test = test || branchAndBound(nextVertex, unvisited, visited,
	            		currentCost+g.getCost(currentVertex, nextVertex),costBetweenTakeUpAndReturn,maximum_cost);
	            visited.remove(nextVertex);
	            unvisited.add(nextVertex);
	        }
			return test;
	    }
		return false;
	}

}
