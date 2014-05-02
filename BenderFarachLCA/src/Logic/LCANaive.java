package Logic;

import Model.INode;
import Model.Tree;

public class LCANaive {

	private INode[] path1, path2;

	public LCANaive(Tree tree) {
		this.path1 = new INode[tree.getN()];
		this.path2 = new INode[tree.getN()];
	}

	public INode LCA(INode node1, INode node2) {
		int count1 = findPath(node1, path1);
		int count2 = findPath(node2, path2);

		while (-1 < count1 && -1 < count2 && path1[count1] == path2[count2]) {
			count1--;
			count2--;
		}

		return path1[count1 + 1];
	}

	/*
	 * Builds a path from the source node to the root. Returns the index of the
	 * root
	 */
	private int findPath(INode source, INode[] path) {
		int count = 0;
		INode parent = source.getParent();
		while (parent != null) {
			path[count++] = parent;
			parent = parent.getParent();
		}
		return count;
	}
}
