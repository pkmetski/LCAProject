package Model;

public interface INode {
	Iterable<INode> getChildren();

	void addChild(INode child);

	void setParent(INode parent);
	INode getParent();
}
