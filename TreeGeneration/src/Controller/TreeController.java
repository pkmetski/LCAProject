package Controller;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.naming.directory.InvalidAttributeValueException;

import Model.INode;
import Model.Tree;

public class TreeController {

	private int B;

	public Tree createTree(int[] args) throws InvalidAttributeValueException {
		// pre-conditions
		// b+1 <= n
		// d <= n
		int N = args[0];
		this.B = args[1];
		int D = args[2];
		if (/* N < B + 1 || */N < D || N < D + B - 1) {
			throw new InvalidAttributeValueException();
		}

		/* The random 2 nodes to be used when testing the algorithms */
		Random rnd = new Random();
		int node1Label = rnd.nextInt(N);
		int node2Label = rnd.nextInt(N);

		Tree tree = new Tree(B);
		while (tree.getN() < N) {
			INode node = getNextNode(tree, D, B);
			INode child = tree.addChild(node);
			if (child.getLabel() == node1Label) {
				tree.setNode1(child);
			} else if (child.getLabel() == node2Label) {
				tree.setNode2(child);
			}
		}
		return tree;
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
			if (node == null) {
				break;
			}
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
			if (node.getChildrenCount() < B) {
				return node;
			}

			for (INode child : node.getChildren()) {
				if (child == null) {
					break;
				}
				q.add(child);
			}
		}
		return root;
	}
}
