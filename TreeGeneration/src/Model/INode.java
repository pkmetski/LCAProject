package Model;

import java.util.List;

public interface INode {

	List<INode> getChildren();

	void addChild(INode child);

	void setParent(INode parent);

	INode getParent();

	int getDepth();

	void setDepth(int depth);
}