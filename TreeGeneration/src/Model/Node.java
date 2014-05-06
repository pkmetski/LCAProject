package Model;

import java.io.Serializable;

public class Node implements INode, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private INode[] children;
	private int count = 0;
	private int label;
	private INode parent;
	private int depth;

	public Node(int label, int childrenCount) {
		this.children = new INode[childrenCount];
		this.label = label;
	}

	public int getLabel() {
		return this.label;
	}

	public INode[] getChildren() {
		return children;
	}

	public int getChildrenCount() {
		return this.count;
	}

	public void addChild(INode child) {
		child.setParent(this);
		children[count++] = child;
	}

	public void setParent(INode parent) {
		this.parent = parent;
	}

	public INode getParent() {
		return this.parent;
	}

	public int getDepth() {
		return this.depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}
