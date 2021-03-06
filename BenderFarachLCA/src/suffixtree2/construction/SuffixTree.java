package suffixtree2.construction;

/**
 * 
 * @author Andrey Naumenko
 * 
 */
// All credit for the construction of the suffix tree goes to Andrey Naumenko
// (indi256 @ github)
// https://github.com/indy256/wikialgo/tree/master/java/src
// https://sites.google.com/site/indy256/algo/suffix_tree

public class SuffixTree {
	static String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890\1\2";
	static int alphabetSize = alphabet.length();

//	static class Node {
//		int depth; // from start of suffix
//		int begin;
//		int end;
//		Node[] children;
//		Node parent;
//		Node suffixLink;
//
//		Node(int begin, int end, int depth, Node parent) {
//			children = new Node[alphabetSize];
//			this.begin = begin;
//			this.end = end;
//			this.parent = parent;
//			this.depth = depth;
//		}
//
//		boolean contains(int d) {
//			return depth <= d && d < depth + (end - begin);
//		}
//	}

	public static Node buildSuffixTree(String s) {
		int n = s.length();
		byte[] a = new byte[n];
		for (int i = 0; i < n; i++) {
			a[i] = (byte) alphabet.indexOf(s.charAt(i));
		}
		int ci = 0; // identifying nodes (current id counter) **
		Node root = new Node(ci, 0, 0, 0, null);
		Node cn = root;
		root.nodes = 0;
		root.nodes++; // counting nodes (reverse counter, if we build a new tree) ***
		// root.suffixLink must be null, but that way it gets more convenient
		// processing
		root.suffixLink = root;
		Node needsSuffixLink = null;
		int lastRule = 0;
		int j = 0;
		for (int i = -1; i < n - 1; i++) {// strings s[j..i] already in tree,
			// add s[i+l] to it.
			int cur = a[i + 1]; // last char of current string
			for (; j <= i + 1; j++) {
				int curDepth = i + 1 - j;
				if (lastRule != 3) {
					cn = cn.suffixLink != null ? cn.suffixLink : cn.parent.suffixLink;
					int k = j + cn.depth;
					while (curDepth > 0 && !cn.contains(curDepth - 1)) {
						k += cn.end - cn.begin;
						cn = cn.children[a[k]];
					}
				}
				if (!cn.contains(curDepth)) { // explicit node
					if (needsSuffixLink != null) {
						needsSuffixLink.suffixLink = cn;
						needsSuffixLink = null;
					}
					if (cn.children[cur] == null) {
						// no extension - add leaf
						ci++; // identifying nodes **
						cn.children[cur] = new Node(ci, i + 1, n, curDepth, cn);
						root.nodes++; // counting nodes ***
						lastRule = 2;
					} else {
						cn = cn.children[cur];
						lastRule = 3; // already exists
						break;
					}
				} else { // implicit node
					int end = cn.begin + curDepth - cn.depth;
					if (a[end] != cur) { // split implicit node here
						ci++; // identifying nodes **
						Node newn = new Node(ci, cn.begin, end, cn.depth, cn.parent);
						root.nodes++; // counting nodes ***
						ci++; // identifying nodes **
						newn.children[cur] = new Node(ci, i + 1, n, curDepth, newn);
						root.nodes++; // counting nodes ***
						newn.children[a[end]] = cn;
						cn.parent.children[a[cn.begin]] = newn;
						if (needsSuffixLink != null) {
							needsSuffixLink.suffixLink = newn;
						}
						cn.begin = end;
						cn.depth = curDepth;
						cn.parent = newn;
						cn = needsSuffixLink = newn;
						lastRule = 2;
					} else if (cn.end != n || cn.begin - cn.depth < j) {
						lastRule = 3;
						break;
					} else {
						lastRule = 1;
					}
				}
			}
		}
		root.suffixLink = null;
		return root;
	}

	// usage example
	static int lcsLength;
	static int lcsBeginIndex;

	// traverse suffix tree to find longest common substring
	public static int findLCS(Node node, int i1, int i2) {
		if (node.begin <= i1 && i1 < node.end) {
			return 1;
		}
		if (node.begin <= i2 && i2 < node.end) {
			return 2;
		}
		int mask = 0;
		for (char f = 0; f < alphabetSize; f++) {
			if (node.children[f] != null) {
				mask |= findLCS(node.children[f], i1, i2);
			}
		}
		if (mask == 3) {
			int curLength = node.depth + node.end - node.begin;
			if (lcsLength < curLength) {
				lcsLength = curLength;
				lcsBeginIndex = node.begin;
			}
		}
		return mask;
	}

	// Usage example
	public static void main2(String[] args) {
		String s1 = "12345";
		String s2 = "124234";
		// build generalized suffix tree (see Gusfield, p.125)
		String s = s1 + '\1' + s2 + '\2';
		Node root = buildSuffixTree(s);
		lcsLength = 0;
		lcsBeginIndex = 0;
		// find longest common substring
		findLCS(root, s1.length(), s1.length() + s2.length() + 1);
		System.out.println(3 == lcsLength);
		System.out.println(s.substring(lcsBeginIndex - 1, lcsBeginIndex
				+ lcsLength - 1));
	}

	// Test the count of nodes ***
	public static void main(String[] args) {
		String s = "ananas\1";
		Node root = buildSuffixTree(s);
		System.out.println("Node count: " + root.nodes);
		root.PrintPretty("", true);
	}
}