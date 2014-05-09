package Controller;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.naming.directory.InvalidAttributeValueException;

import Model.INode;
import Model.Tree;

public class TreeController {

	private int N, B, D;
	private Random rnd = new Random();
	private Tree tree;

	public Tree createTree(int[] args) throws InvalidAttributeValueException {
		// pre-conditions
		// b+1 <= n
		// d <= n
		this.N = args[0];
		this.B = args[1];
		this.D = args[2];
		if (/* N < B + 1 || */N < D || N < D + B - 1) {
			throw new InvalidAttributeValueException();
		}

		this.tree = new Tree(N, B, D);
		while (tree.getN() < N) {
			INode parentNode = getNextNode(tree, D, B);
			tree.addChild(parentNode);
		}
		return tree;
	}

	public INode getRandomNode(Tree tree, int N) {
		return tree.getNode(rnd.nextInt(N));
	}

	private INode getNextNode(Tree tree, int D, int B) {
		// if D is not reached
		if (tree.getD() < D) {
			// do a depth first on the tree
			// at this point, B = 1
			return tree.getLastNode();
			// return dfs(tree.getRoot(), D);
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
			// less than needed, return it
			if (node.getChildrenCount() < B) {
				return node;
			} else {
				for (INode child : node.getChildren()) {
					if (child == null) {
						break;
					} else {
						if (B <= child.getChildrenCount())
							q.add(child);
						else
							return child;
					}
				}
			}
		}
		return root;
	}
}
