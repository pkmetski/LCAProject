package Model;

import java.util.ArrayList;
import java.util.List;

public class Node implements INode {

	private ArrayList<INode> children;
	private String label;
	private INode parent;
	private int depth;

	public Node(String label) {
		this.children = new ArrayList<INode>();
		this.label = label;
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
