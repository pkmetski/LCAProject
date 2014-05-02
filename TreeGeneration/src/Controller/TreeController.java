package Controller;

import java.util.LinkedList;
import java.util.Queue;

import javax.naming.directory.InvalidAttributeValueException;

import Model.INode;
import Model.Tree;

public class TreeController {

	public TreeController() {

	}

	public Tree createTree(int[] args) throws InvalidAttributeValueException {
		// pre-conditions
		// b+1 <= n
		// d <= n
		int N = args[0];
		int B = args[1];
		int D = args[2];
		if (/* N < B + 1 || */N < D || N < D + B) {
			throw new InvalidAttributeValueException();
		}

		Tree tree = new Tree();
		while (tree.getN() < N) {
			INode node = getNextNode(tree, D, B);
			tree.addChild(node);
		}

		return tree;
	}

	public INode getNode1() {
		return null;/* FIND THE APPROPRIATE NODE */
	}

	public INode getNode2() {
		return null;/* FIND THE APPROPRIATE NODE */
	}

	private INode getNextNode(Tree tree, int D, int B) {
		// if D is not reached
		if (tree.getD() < D) {
			// do a depth first on the tree
			// at this point, B = 1

			return dfs(tree.getRoot(), D);
		}
		// if D is reached
		else {
			// now we need to keep track of B
			// do a breadth first on the tree
			// return the first node,
			// which would not violate the B constraint

			return bfs(tree.getRoot(), B);
		}
	}

	private INode dfs(INode root, int D) {
		// if the max depth is reached
		if (root.getDepth() - 1 == D) {
			return root;
		}

		for (INode node : root.getChildren()) {
			root = dfs(node, D);
		}
		return root;
	}

	private INode bfs(INode root, int B) {
		Queue<INode> q = new LinkedList<INode>();
		q.add(root);

		while (0 < q.size()) {
			INode node = q.poll();

			// if this node's branching factor is one
			// less than we need, return it
			if (node.getChildren().size() < B) {
				return node;
			}

			for (INode child : node.getChildren()) {
				q.add(child);
			}
		}
		return root;
	}
}
