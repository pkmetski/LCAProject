package Logic;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import Model.INode;
import Model.Node;

public class TreeController {

	public INode getTree() {

		INode node6 = new Node("6");
		INode node5 = new Node("5");
		INode node4 = new Node("4");
		INode node3 = new Node("3");

		INode node2 = new Node("2");
		node2.addChild(node6);
		node2.addChild(node5);
		INode node1 = new Node("1");
		node1.addChild(node4);
		node1.addChild(node3);

		INode root = new Node("0");
		root.addChild(node2);
		root.addChild(node1);

		return root;
	}
}
