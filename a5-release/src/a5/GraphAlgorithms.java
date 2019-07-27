package a5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import common.NotImplementedError;
import graph.Edge;
import graph.Node;
import graph.LabeledEdge;

import a4.Heap;

/** We've provided depth-first search as an example; you need to implement Dijkstra's algorithm.
 */
public class GraphAlgorithms  {
	/** Return the Nodes reachable from start in depth-first-search order */
	public static <N extends Node<N,E>, E extends Edge<N,E>>
	List<N> dfs(N start) {
		
		Stack<N> worklist = new Stack<N>();
		worklist.add(start);
		
		Set<N>   visited  = new HashSet<N>();
		List<N>  result   = new ArrayList<N>();
		while (!worklist.isEmpty()) {
			// invariants:
			//    - everything in visited has a path from start to it
			//    - everything in worklist has a path from start to it
			//      that only traverses visited nodes
			//    - nothing in the worklist is visited
			N next = worklist.pop();
			visited.add(next);
			result.add(next);
			for (N neighbor : next.outgoing().keySet())
				if (!visited.contains(neighbor))
					worklist.add(neighbor);
		}
		return result;
	}
	
	/**
	 * Return a minimal path from start to end.  This method should return as
	 * soon as the shortest path to end is known; it should not continue to search
	 * the graph after that. 
	 * 
	 * @param <N> The type of nodes in the graph
	 * @param <E> The type of edges in the graph; the weights are given by e.label()
	 * @param start The node to search from
	 * @param end   The node to find
	 */
	public static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	List<N> shortestPath(N start, N end) {
		// To check the neighbors of a node n use:
		// n.outgoing().keySet()
		
		// Set containing all the visited nodes.
		// To check if a node n is unvisited use:
		// !visited.contains(n)
		Set<N> visited = new HashSet<N>();
		visited.add(start);
		// distances stores the distance from the start Node to Node N as a double.
		Heap<N, Integer> 	distances 	= new Heap<N, Integer>(Comparator.reverseOrder());
		distances.add(start, 0);
		// parents stores the parent of a Node n as a Node.
		HashMap<N, N> 		parents 	= new HashMap<N, N>();
		// all_visited is a boolean determining if all nodes in the graph have been visited
		boolean all_visited = false;
		// node_curr is the current node of Dijkstra's algorithm.
		N node_curr = start;
		
		while (!all_visited) {
			
//			Node node_min = neighbors_dist.poll();
		}
		

		 
		
		return null;
	}
	
	
	/** Returns the edge weight of the edge between node_from and node_to */
	private static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	int getEdgeWeight(N node_from, N node_to) {
		return node_from.outgoing().get(node_to).label();
	}
	
	/** Returns a heap of nodes with priority of edge weight
	 *  of the edge between node_from and its neighbors. */
	private static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	Heap<N, Integer> getMinEdge(N node_from) {
		Heap<N, Integer> neighbors_dist 	= new Heap<N, Integer>(Comparator.reverseOrder());
		for (N neighbor : node_from.outgoing().keySet()) {
			neighbors_dist.add(neighbor, getEdgeWeight(node_from, neighbor));
		}
		return null;
	}
	
}
