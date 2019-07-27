package a5.testing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import gui.Adapter;
import gui.Graph;
import gui.NodeData;
import gui.TextIO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PathsTester {

	@Test
	/** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and no edges. <br>
	 * Test a 1-node path from Ithaca to Ithaca and <br>
	 * an empty shortest path from Ithaca to Truck Depot */
	public void test10OneNodeBoard() {
		Graph g = getGraph("info/Maps/OneNodeBoard.txt");
		NodeData n= g.getNode("Ithaca");
		List<NodeData> path= Adapter.shortest(n, n);
		List<NodeData> expected= new LinkedList<>();
		expected.add(n);
		assertEquals(expected, path);
		// System.out.println("Random: "+new Random(128).nextInt());
	}

	@Test
	/** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and no edges. <br>
	 * Test a 1-node path from Ithaca to Ithaca and <br>
	 * an empty shortest path from Ithaca to Truck Depot */
	public void test20NoEdges() {
		Graph g= getGraph("info/Maps/TwoNodeNoEdge.txt");
		NodeData n= g.getNode("Ithaca");
		List<NodeData> path= Adapter.shortest(n, n);
		List<NodeData> expected= new LinkedList<>();
		expected.add(n);
		assertEquals(expected, path);

		NodeData n1= g.getNode("Truck Depot");
		List<NodeData> path1= Adapter.shortest(n, n1);
		List<NodeData> expected1= new LinkedList<>();
		assertEquals(expected1, path1);
	}

	@Test
	/** Get a two-node graph with two nodes "Ithaca" and "Truck Depot" and <br>
	 * an edge between them. <br>
	 * Test a 1-node path from Ithaca to Ithaca, <br>
	 * a 2-node shortest path from Ithaca to Truck Depot, and <br>
	 * a 2-node shortest path from Truck Depot to Ithaca */
	public void test30TwoNodeOneEdge() {
		Graph g= getGraph("info/Maps/TwoNodeBoard.txt");
		NodeData n= g.getNode("Ithaca");
		List<NodeData> path= Adapter.shortest(n, n);
		List<NodeData> expected= new LinkedList<>();
		expected.add(n);
		assertEquals(expected, path);

		NodeData n1= g.getNode("Truck Depot");
		List<NodeData> path1= Adapter.shortest(n, n1);
		List<NodeData> expected1= new LinkedList<>();
		expected1.add(n);
		expected1.add(n1);
		assertEquals(expected1, path1);

		List<NodeData> path2= Adapter.shortest(n1, n);
		List<NodeData> expected2= new LinkedList<>();
		expected2.add(n1);
		expected2.add(n);
		assertEquals(expected2, path2);
	}

	@Test
	/** Test all shortest paths on map TestBoard1.txt */
	public void test40MapTestBoard1() {
		Graph g= getGraph("info/Maps/TestBoard1.txt");
		PathData pd= new PathData("info/Maps/TestBoard1distances.txt", g);
		assertEquals(3, pd.size);
		checkAllShortestPaths(g, pd);
	}

	@Test
	/** Test all shortest paths on map seed16.txt */
	public void test50MapSeed16() {
		Graph g= getGraph("info/Maps/Seed16.txt");
		PathData pd= new PathData("info/Maps/Seed16distances.txt", g);
		assertEquals(6, pd.size);
		checkAllShortestPaths(g, pd);
	}

	@Test
	/** Test all shortest paths on map ...t */
	public void test60MapBoard3() {
		Graph g= getGraph("info/Maps/Board3.txt");
		PathData pd= new PathData("info/Maps/Board3distances.txt", g);
		assertEquals(10, pd.size);
		checkAllShortestPaths(g, pd);
	}

	@Test
	/** Test all shortest paths on map ...t */
	public void test70MapTestBoard2() {
		Graph g= getGraph("info/Maps/TestBoard2.txt");
		PathData pd= new PathData("info/Maps/TestBoard2distances.txt", g);
		assertEquals(34, pd.size);
		checkAllShortestPaths(g, pd);
	}

	@Test
	/** Test all shortest paths on map seeded with 128 */
	public void test80MapSeeded128() {
		Graph g= getGraph("info/Maps/seed128.txt");
		PathData pd= new PathData("info/Maps/seed128distances.txt", g);
		assertEquals(50, pd.size);
		checkAllShortestPaths(g, pd);
	}

	/** Check the shortest paths in g from each node to each node, as given by <br>
	 * Adapter.shortestPaths, matches that in pd. <br>
	 * Use the ordering of nodes as given in pd.names. */
	public void checkAllShortestPaths(Graph g, PathData pd) {
		for (int r= 0; r < pd.size; r= r + 1) {
			for (int c= 0; c < pd.size; c= c + 1) {
				// check shortest path distance from node r to node c
				List<NodeData> list= Adapter.shortest(pd.nodes[r], pd.nodes[c]);
				assertEquals(pd.dist[r][c], pathSum(list));

				// check that first node of path and last node of path are correct
				if (list.size() > 0) {
					assertEquals(pd.nodes[r], list.get(0));
					assertEquals(pd.nodes[c], list.get(list.size() - 1));
				}
			}
		}

	}

	/** Return a graph for file named s in the info. */
	private Graph getGraph(String s) {
		try {
			return Graph.getJsonGraph(new JSONObject(TextIO.read(new File(s))));
		} catch (IOException e) {
			throw new RuntimeException("IO Exception reading in graph " + s);
		}
	}
	
	/** Return the sum of the weights of the edges on path pa. <br>
	 * Precondition: pa contains at least 1 node. <br>
	 * If 1 node, it's a path of length 0, i.e. with no edges. */
	private static int pathSum(List<NodeData> pa) {
		synchronized (pa) {
			NodeData v= null;
			int sum= 0;
			// invariant: if v is null, n is the first node of the path.<br>
			// ......... if v is not null, v is the predecessor of n on the path.
			// sum = sum of weights on edges from first node to v
			for (NodeData n : pa) {
				if (v != null) sum= sum + v.getEdge(n).length;
				v= n;
			}
			return sum;
		}
	}

}
