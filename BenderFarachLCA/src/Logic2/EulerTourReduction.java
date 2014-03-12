package Logic2;

/*****************************************************
 * Reduce the LCA problem to RMQ (perform the Euler tour) that results in 3 arrays:
 * 		E[] of nodes(their labels) with size 2*n-1;
 * 		L[] of levels(distance from the root) with size 2*n-1;
 * 		R[] of representatives' first occurrence with size n.
 * 		
 *****************************************************/


import suffixtree.construction.Node;

public class EulerTourReduction {

	private int n;
	private Node[] E;
	private int[] L;
	private int[] R;
	private int nodeIndex = -1;
	private int level = -1;

	public EulerTourReduction(int n) {
		this.n = n;
		E = new Node[2*n-1];
		L = new int[2*n-1];
		R = new int[n];
	}

	public void process(Node root) {
		eulerTour(root);
	}
	
	
	
	private void eulerTour(Node parent) {
		L[++nodeIndex] = ++level;
		E[nodeIndex] = parent;

//		if (!R.containsKey(parent)) {
//			R.put(parent, nodeIndex);
//		}

		for (Node child : parent.children()) {
			eulerTour(child);
			L[++nodeIndex] = --level;
			E[nodeIndex] = parent;
		}
	}

	public int[] getR() {
		return R;
	}

	public Node[] getE() {
		return E;
	}

	public int[] getL() {
		return L;
	}
}
