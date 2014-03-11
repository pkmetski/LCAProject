package Logic;

import java.util.HashMap;
import java.util.Map;

import Model.INode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class EulerTourController {

	private int n;
	private INode[] E;// nodes visited in euler tour order
	private int[] L;// node level
	private Map<INode, Integer> R;// representative of node
	private int nodeIndex = -1;
	private int level = -1;

	public EulerTourController(int n) {
		this.n = n;
		this.E = new INode[(2 * n) - 1];
		this.L = new int[(2 * n) - 1];
		this.R = new HashMap<INode, Integer>(n);
	}

	public void process(INode root) {
		eulerTour(root);
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
