import java.util.Arrays;

import Logic2.EulerTourReduction;
import Logic2.RMQSolution;
import suffixtree.construction.SuffixTree;
import suffixtree.construction.Node;

public class Main2 {

	public static void main(String[] args) {
		String s = "ananas\1";
		Node root = SuffixTree.buildSuffixTree(s);

		EulerTourReduction et = new EulerTourReduction(root.nodes);
//		et.euler_tour(root);
		et.euler_tour_plamen_jesper(root);
		
		RMQSolution rmq = new RMQSolution(et.getE(),et.getL(),et.getR());
//		int answer = rmq.solveA(4, 8);
		
		// checks - to be deleted later.......................................
		// check tree node ordering
		root.PrintPretty("", true);
		
		// separate the tree and the list checks
		//System.out.println();
		
		// check whether all edges were added correctly
		//printArray(et.edge_list);
		
		// separate the tree and the list checks
		//System.out.println();
		
		// check adjacency lists (next)
		//et.checkAdjacency(); // if step4 not performed
		
		// separate the tree and the list checks
		//System.out.println();
		
		// check mapping (first)
		//et.checkMapping();
		
		// check tour of nodes visited (E)
		System.out.println();
		et.checkE();
		
		// check tour of node levels (L)
		System.out.println();
		et.checkL();
		
		// check tour of node levels (R)
		System.out.println();
		et.checkR();
		
		// check sparse tree (st)
		//System.out.println();
		//rmq.checkSparseTree();
		
		// check answer
//		System.out.println();
//		System.out.println(answer);
		
		// check partitioning
		System.out.println();
		rmq.solveB(4, 8);
		rmq.checkPartition();
		rmq.checkHashMap();
		
		// ...................................................................

	}
	
	/*
	 * Check matrix filled correctly
	 * http://stackoverflow.com/questions/17118664/printing-a-two-dimensional-matrix-array-java
	 */
	public static void printArray(int matrix[][]) {
	    for (int[] row : matrix) 
	        System.out.println(Arrays.toString(row));
	}
	
}
