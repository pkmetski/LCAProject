package Model;

import java.io.Serializable;

public class Tree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int N;
	private int B;
	private int D;
	private INode root;
	private int nodeId = 0;
	private INode node1, node2;

	public Tree(int b) {
		this.N = 0;
		this.B = b;
		this.D = 0;
		this.node1 = null;
		this.node2 = null;
		addRoot(new Node(nodeId++, b));
	}

	public INode getRoot() {
		return root;
	}

	private void addRoot(INode root) {
		this.root = root;
		this.root.setDepth(0);

		/* Update tree size */
		this.N++;
	}

	public INode addChild(INode parent) {
		INode child = new Node(nodeId++, this.B);
		parent.addChild(child);
		child.setDepth(parent.getDepth() + 1);
		child.setParent(parent);

		/* Update tree size */
		this.N++;

		/* Update tree's depth */
		if (this.D < child.getDepth()) {
			this.D = child.getDepth();
		}

		/* Update tree's branching factor */
		int childrenCount = parent.getChildrenCount();
		if (this.B < childrenCount) {
			this.B = childrenCount;
		}
		return child;
	}

	public int getN() {
		return this.N;
	}

	public int getB() {
		return this.B;
	}

	public int getD() {
		return this.D;
	}

	public INode getNode1() {
		return this.node1;
	}

	public void setNode1(INode node) {
		this.node1 = node;
	}

	public INode getNode2() {
		return this.node2;
	}

	public void setNode2(INode node) {
		this.node2 = node;
	}
}
