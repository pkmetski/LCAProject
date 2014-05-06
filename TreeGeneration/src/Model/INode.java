package Model;

public interface INode{

	int getLabel();

	INode[] getChildren();
	
	int getChildrenCount();

	void addChild(INode child);

	void setParent(INode parent);

	INode getParent();

	int getDepth();

	void setDepth(int depth);
}
