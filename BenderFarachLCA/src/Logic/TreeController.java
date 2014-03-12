package Logic;

import Model.INode;
import Model.Node;

public class TreeController {

	private INode[] nodes;

	public TreeController(int n) {
		nodes = new INode[n];
	}

	public INode getTree() {

		INode node6 = new Node("G");
		INode node5 = new Node("F");
		INode node4 = new Node("E");
		INode node3 = new Node("D");

		INode node2 = new Node("B");
		node2.addChild(node6);
		node2.addChild(node5);
		INode node1 = new Node("C");
		node1.addChild(node4);
		node1.addChild(node3);

		INode root = new Node("A");
		root.addChild(node2);
		root.addChild(node1);

		int count = 0;
		nodes[count++] = root;
		nodes[count++] = node1;
		nodes[count++] = node2;
		nodes[count++] = node3;
		nodes[count++] = node4;
		nodes[count++] = node5;
		nodes[count++] = node6;

		return root;
	}

	public INode getNode(int index) {
		return nodes[index];
	}
}
