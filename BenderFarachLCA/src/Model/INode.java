package Model;

public interface INode {
	public String getLabel();

	Iterable<INode> getChildren();

	void addChild(INode child);
}
