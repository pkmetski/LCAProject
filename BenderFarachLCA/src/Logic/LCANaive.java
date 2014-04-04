package Logic;

import Model.INode;

public class LCANaive {

	private INode tree;
	private int nodeCount;
	private INode[] path1, path2;
	private int count1, count2;

	public LCANaive(INode tree, int nodeCount) {
		this.tree = tree;
		this.nodeCount = nodeCount;
		this.path1 = new INode[nodeCount];
		this.path2 = new INode[nodeCount];
	}

	public INode RMQ(INode node1, INode node2) {
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
