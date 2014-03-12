import java.util.Arrays;

import Logic2.EulerTourReduction;
import Logic2.RMQController;
import suffixtree.construction.SuffixTree;
import suffixtree.construction.Node;

public class Main2 {

	public static void main(String[] args) {
		String s = "ananas\1";
		Node root = SuffixTree.buildSuffixTree(s);

		EulerTourReduction et = new EulerTourReduction(root.nodes);
		et.euler_tour(root);
		
		// check whether all edges were added correctly
		printArray(et.edge_list);

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
