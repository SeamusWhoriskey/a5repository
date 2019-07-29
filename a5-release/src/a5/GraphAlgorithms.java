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
		
		// Set unvisited contains all the unvisited nodes.
		Set<N> unvisited = new HashSet<N>(dfs(start));
		
		// If there is no connection from start to end, return an empty List.
		if (!unvisited.contains(end)) {
			return new ArrayList<N>();
		}
		
		// HashMap distances stores the distance 
		// from Node start to Node N as a Double.
//		HashMap<N, Double> 	distances 	= new HashMap<N, Double>();
		
		// parents stores the parent of a Node n as a Node.
		HashMap<N, N> 		parents 	= new HashMap<N, N>();
		// Initializations of values for distances and parents.
		Heap<N, Double> neighbor_dist = new Heap<N, Double>(Comparator.reverseOrder());
		for (N neighbor : unvisited) {
			neighbor_dist.add(neighbor, Double.POSITIVE_INFINITY);
			// set all distances to positive infinity
//			distances.put(neighbor, Double.POSITIVE_INFINITY);	
			// set the parent of each neighbor to null
			parents.put(neighbor, null);	
		}
		// set the distance of the start node to itself as 0.
//		distances.put(start, 0.0);
		neighbor_dist.changePriority(start, 0.0);
		
		
//		Heap<N, Double> neighbor_dist = getWeightHeap(distances, unvisited);
		
		// While there are still unvisited nodes, 
		while (!unvisited.isEmpty()) {
			// neighbor_dist is a Heap that contains the distance 
			// from curr_node to each neighbor of curr_node.
//			Heap<N, Double> neighbor_dist = getWeightHeap(distances, unvisited);			
			
			// node_curr denotes the current node of Dijkstra's algorithm.
			N node_curr = neighbor_dist.peek();
			
			Double dist_curr = neighbor_dist.getPriority(node_curr);
			neighbor_dist.poll();
			
			if (!neighbor_dist.checkHeap(0)) {
				neighbor_dist.heapifyDown(0);
				System.out.println(neighbor_dist.toString());
				
			}
			
			
			// If the current node's closest neighbor is not connected, break
			if (dist_curr == Double.POSITIVE_INFINITY) {
				unvisited.remove(node_curr);
				break; 	
			}
			
			
			unvisited.remove(node_curr);
			
			for (N neighbor : node_curr.outgoing().keySet()) {
				if (unvisited.contains(neighbor)) {
					double new_weight = dist_curr
										+ getEdgeWeight(node_curr, neighbor);
					if (new_weight < neighbor_dist.getPriority(neighbor))  {
						neighbor_dist.changePriority(neighbor, new_weight);
//						distances.put(neighbor, new_weight);
						parents.put(neighbor, node_curr);
					}
				}
			}

			
		}

		return parentsToList(parents, start, end);

	}
	
	
	/** Returns the edge weight of the edge between node_from and node_to */
	private static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	double getEdgeWeight(N node_from, N node_to) {
		return node_from.outgoing().get(node_to).label();
	}
	
	/** Returns a Heap of nodes with priority of edge weight of the edge 
	 * between node_from and its neighbors that are in the Set unvis.
	 * 
	 *  @param dist
	 *  @param unvis */
	private static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	Heap<N, Double> getWeightHeap(HashMap<N, Double> dist, Set<N> unvis) {
		// n_dist is the Heap to be returned. Will contain unvisited Nodes sorted
		// in order of least distance to greatest distance from the start node.
		Heap<N, Double> n_dist 	= new Heap<N, Double>(Comparator.reverseOrder());
		// For each unvisited node,
		for (N node : unvis) {
			// Add node to n_dist with priority given in dist.
			n_dist.add(node, dist.get(node));
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
			out_list.add(last_added);
//			System.out.println(start.toString() + " " + end.toString() + " " + out_list.toString());
//			break;
		}
		Collections.reverse(out_list);
		return out_list;
	}
	
}	
