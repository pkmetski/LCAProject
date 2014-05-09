package Model;

import java.io.Serializable;

public class Tree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int currentN;
	private int B;
	private int currentD;
	private int N;
	private int D;
	private INode root;
	private int nodeId = 0;
	private INode[] nodes;

	public Tree(int N, int B, int D) {
		this.N = N;
		this.B = B;
		this.D = D;
		this.currentN = 0;
		this.currentD = 0;
		this.nodes = new INode[N];
		addRoot(createNode());
	}

	private INode createNode() {
		INode node = new Node(this.nodeId, this.B);
		nodes[this.nodeId++] = node;
		return node;
	}

	public INode getRoot() {
		return root;
	}

	private void addRoot(INode root) {
		this.root = root;
		this.root.setDepth(0);

		/* Update tree size */
		this.currentN++;
	}

	public INode addChild(INode parent) {
		INode child = createNode();
		parent.addChild(child);
		child.setDepth(parent.getDepth() + 1);
		child.setParent(parent);

		/* Update tree size */
		this.currentN++;

		/* Update tree's depth */
		if (this.currentD < child.getDepth()) {
			this.currentD = child.getDepth();
		}

		/* Update tree's branching factor */
		int childrenCount = parent.getChildrenCount();
		if (this.B < childrenCount) {
			this.B = childrenCount;
		}
		return child;
	}

	public int getN() {
		return this.currentN;
	}

	public int getB() {
		return this.B;
	}

	public int getD() {
		return this.currentD;
	}

	public INode getNode(int index) {
		return nodes[index];
	}

	public INode getLastNode() {
		return nodes[this.nodeId - 1];
	}
}
