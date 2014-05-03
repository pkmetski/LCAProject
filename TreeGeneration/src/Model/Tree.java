package Model;

public class Tree {

	private int N;
	private int B;
	private int D;
	private INode root;
	private int nodeId = 0;

	public Tree() {
		this.N = 0;
		this.B = 0;
		this.D = 0;
		addRoot(new Node(nodeId++));
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
		INode child = new Node(nodeId++);
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
		int childrenCount = parent.getChildren().size();
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
}
