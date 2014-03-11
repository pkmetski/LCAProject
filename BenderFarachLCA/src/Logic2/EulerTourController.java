package Logic2;

import java.util.HashMap;
import java.util.Map;

import suffixtree.construction.Node;

public class EulerTourController {

	private int n;
	private Node[] E;// nodes visited in euler tour order
	private int[] L;// node level
	private Map<Node, Integer> R;// representative of node
	private int nodeIndex = -1;
	private int level = -1;

	public EulerTourController(int n) {
		this.n = n;
		this.E = new Node[(2 * n) - 1];
		this.L = new int[(2 * n) - 1];
		this.R = new HashMap<Node, Integer>(n);
	}

	public void process(Node root) {
		eulerTour(root);
	}

	private void eulerTour(Node parent) {
		L[++nodeIndex] = ++level;
		E[nodeIndex] = parent;

		if (!R.containsKey(parent)) {
			R.put(parent, nodeIndex);
		}

		for (Node child : parent.children()) {
			eulerTour(child);
			L[++nodeIndex] = --level;
			E[nodeIndex] = parent;
		}
	}

	public Map<Node, Integer> getR() {
		return R;
	}

	public Node[] getE() {
		return E;
	}

	public int[] getL() {
		return L;
	}
}
