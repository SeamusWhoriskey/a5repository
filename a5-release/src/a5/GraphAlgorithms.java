package a5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
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
//		System.out.println(start.toString() + " " + end.toString());
		if (start.equals(end)) {
			List<N> out_list = new ArrayList<N>();
			out_list.add(start);
			return out_list;
		}
		// To check the neighbors of a node n use:
		// n.outgoing().keySet()
		
		// Set containing all the visited nodes.
		// To check if a node n is unvisited use:
		// !visited.contains(n)
		Set<N> unvisited = new HashSet<N>(dfs(start));
		// distances stores the distance from the start Node to Node N as a double.
		HashMap<N, Double> 	distances 	= new HashMap<N, Double>();
		
		// parents stores the parent of a Node n as a Node.
		HashMap<N, N> 		parents 	= new HashMap<N, N>();
		for (N neighbor : unvisited) {
			distances.put(neighbor, Double.POSITIVE_INFINITY);	
			parents.put(neighbor, null);	
		}
		distances.put(start, 0.0);
		// node_curr is the current node of Dijkstra's algorithm.
		N node_curr;
		// neighbor_dist is a Heap that will contain the distance from curr_node to each neighbor 
		Heap<N, Double> neighbor_dist = new Heap<N, Double>(Comparator.reverseOrder());
		// path complete ADSLFHADHJFBLA
		boolean path_complete = true;
		
		while (!unvisited.isEmpty()) {
			
			
			neighbor_dist = getWeightHeap(distances, unvisited);
			// If there are no unvisited nodes that are a neighbor of the current node,
			if (neighbor_dist.size() == 0) {
				path_complete = false;
				break; 	
			}
			node_curr = neighbor_dist.poll();

			// If the current node's closest neighbor is not connected, pass
			if (distances.get(node_curr) == Double.POSITIVE_INFINITY) {
				path_complete = false;
				break; 	
			}
			
			
			unvisited.remove(node_curr);
			
			for (N neighbor : node_curr.outgoing().keySet()) {
				if (unvisited.contains(neighbor)) {
					double new_weight = distances.get(node_curr) + getEdgeWeight(node_curr, neighbor);
					if (!distances.containsKey(neighbor) || new_weight < distances.get(neighbor))  {
						distances.put(neighbor, new_weight);
						parents.put(neighbor, node_curr);
					}
				}
			}
//			System.out.println("unvisited list: "+ unvisited.toString());

			
		}
		
		if (path_complete) {
			List<N> n = parentsToList(parents, start, end);
//			System.out.println("Out list: " + n.toString());
			return n;
		}
//		System.out.println(path_complete);
		return new ArrayList<N>();
	}
	
	
	/** Returns the edge weight of the edge between node_from and node_to */
	private static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	double getEdgeWeight(N node_from, N node_to) {
		return node_from.outgoing().get(node_to).label();
	}
	
	/** Returns a heap of nodes with priority of edge weight of the edge 
	 * between node_from and its neighbors that are not in the Set vis. */
	private static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	Heap<N, Double> getWeightHeap(HashMap<N, Double> dist, Set<N> unvis) {
		Heap<N, Double> n_dist 	= new Heap<N, Double>(Comparator.reverseOrder());
		for (N neighbor : unvis) {
			n_dist.add(neighbor, dist.get(neighbor));
		}
		return n_dist;
	}
	
	private static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	List<N> parentsToList(HashMap<N, N> parents, N start, N end) {
		List<N> out_list = new ArrayList<N>();
		out_list.add(end);
		N last_added = end;
		while (!out_list.contains(start)) {
			last_added = parents.get(last_added);
			if (last_added == null) {
				return new ArrayList<N>();
			}
			out_list.add(last_added);
			

//			System.out.println(out_list.toString());
		}
		Collections.reverse(out_list);
		return out_list;
	}
	
}
