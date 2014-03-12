package suffixtree.construction;

//import java.util.Arrays;

import Model.INode;
import suffixtree.construction.Node;

public class Node implements INode{
	String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890\1\2";
	int alphabetSize = alphabet.length();
	
	public int nodes; // counting nodes ***
	public int id; // identify the tree nodes so that when we pick 2 of them we can find LCA of them
	
	int depth; // from start of suffix
	int begin;
	int end;
	Node[] children;
	Node parent;
	Node suffixLink;

	Node(int id, int begin, int end, int depth, Node parent) {
		this.id = id;
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
	
	
	/*
	 * Adapted from http://stackoverflow.com/questions/1649027/how-do-i-print-out-a-tree-structure
	 * It is used with a representational purpose when checking the outcome (whether we've found the right node).
	 */
	public void PrintPretty(String indent, boolean last){
		System.out.print(indent);
		if (last){
			System.out.print("\\-");
			indent += "  ";
			}
		else{
			System.out.print("|-");
			indent += "| ";
			}
		System.out.println(id);

		for (int i = 0; i < children.length; i++)
			if(children[i]!=null) children[i].PrintPretty(indent, i == children.length - 1);
	}

}
