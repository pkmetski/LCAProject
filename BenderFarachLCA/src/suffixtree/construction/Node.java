package suffixtree.construction;

import java.util.Arrays;

import Model.INode;
import suffixtree.construction.Node;

public class Node implements INode{
	String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890\1\2";
	int alphabetSize = alphabet.length();
	
	int depth; // from start of suffix
	int begin;
	int end;
	Node[] children;
	Node parent;
	Node suffixLink;

	Node(int begin, int end, int depth, Node parent) {
		children = new Node[alphabetSize];
		this.begin = begin;
		this.end = end;
		this.parent = parent;
		this.depth = depth;
	}

	boolean contains(int d) {
		return depth <= d && d < depth + (end - begin);
	}

	@Override
	public Iterable<INode> getChildren() {
//		Iterable<INode> iterable= Arrays.asList(children); // http://stackoverflow.com/questions/10335662/convert-java-array-to-iterable
		return null;
	}

	@Override
	public void addChild(INode child) {
		// TODO Auto-generated method stub
		
	}
	
	public Node[] children(){
		return children;
	}

}
