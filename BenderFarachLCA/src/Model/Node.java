package Model;

import java.util.ArrayList;

public class Node implements INode {

	private ArrayList<INode> children;
	private String label;

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
		children.add(child);
	}

	@Override
	public String getLabel() {
		return this.label;
	}

}
