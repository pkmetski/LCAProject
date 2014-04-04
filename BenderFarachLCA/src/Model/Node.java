package Model;

import java.util.ArrayList;

public class Node implements INode {

	private ArrayList<INode> children;
	private String label;
	private INode parent;

	public Node(String label) {
		this.children = new ArrayList<INode>();
		this.label = label;
	}

	@Override
	public Iterable<INode> getChildren() {
		return children;
	}

	@Override
	public void addChild(INode child) {
		child.setParent(this);
		children.add(child);
	}

	@Override
	public void setParent(INode parent) {
		this.parent = parent;
	}

	@Override
	public INode getParent() {
		return this.parent;
	}
}
