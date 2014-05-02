package Logic;

import java.util.HashMap;
import java.util.Map;

import Model.INode;
import Model.Tree;

public class EulerTourController {

	private INode[] E;// nodes visited in euler tour order
	private int[] L;// node level
	private Map<INode, Integer> R;// representative of node
	private int nodeIndex = -1;
	private int level = -1;
	private Tree tree;

	public EulerTourController(Tree tree) {
		this.tree = tree;
		this.E = new INode[(2 * tree.getN()) - 1];
		this.L = new int[(2 * tree.getN()) - 1];
		this.R = new HashMap<INode, Integer>(tree.getN());
	}

	public void preprocess() {
		eulerTour(tree.getRoot());
	}

	private void eulerTour(INode parent) {
		L[++nodeIndex] = ++level;
		E[nodeIndex] = parent;

		if (!R.containsKey(parent)) {
			R.put(parent, nodeIndex);
		}

		for (INode child : parent.getChildren()) {
			eulerTour(child);
			L[++nodeIndex] = --level;
			E[nodeIndex] = parent;
		}
	}

	public Map<INode, Integer> getR() {
		return R;
	}

	public INode[] getE() {
		return E;
	}

	public int[] getL() {
		return L;
	}
}
