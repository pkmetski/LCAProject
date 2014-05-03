package Model;

import java.util.ArrayList;
import java.util.List;

public class Node implements INode {

	private ArrayList<INode> children;
	private int label;
	private INode parent;
	private int depth;

	public Node(int label) {
		this.children = new ArrayList<INode>();
		this.label = label;
	}

	public int getLabel() {
		return this.label;
	}

	public List<INode> getChildren() {
		return children;
	}

	public void addChild(INode child) {
		child.setParent(this);
		children.add(child);
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
