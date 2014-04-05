package Logic2;
import java.util.Arrays;

import suffixtree2.construction.Node;

public class NaiveSolution {
	
	private Node tree;
	private int u;
	private int v;
	private int[] path_u;
	private int[] path_v;
	private boolean found_u = false;
	private boolean found_v = false;
	private int i;
	
	public NaiveSolution(Node root){
		tree = root;
	}
	
	public int answer;
	
	/*
	 * Find the path from root to u
	 * Find the path from root to v
	 * Compare path u with path v until first difference
	 */
	public void solve(int u, int v){
		this.u = u;
		this.v = v;
		path_u = new int[tree.nodes];
		path_v = new int[tree.nodes];
		
		path_u[0] = tree.id;
		path_v[0] = tree.id;
		
		// find both path_u and path_v
		traverseTree(tree);
		
		int lca = -1;
		for(int j = 0; j < tree.nodes; j++){
			if(path_u[j] == path_v[j]) lca = path_u[j];
			else break;
		}
		
		answer = lca;
	}
	
	public void checkPaths(){
		System.out.println();
		System.out.println(Arrays.toString(path_u));
		System.out.println();
		System.out.println(Arrays.toString(path_v));
	}
	
	private void traverseTree(Node root) {
		
		// write if node not already reached
		if(!found_u) path_u[i] = root.id;
		if(!found_v) path_v[i] = root.id;
		
		// determine whether to write
		if(root.id == u)found_u = true;
		if(root.id == v)found_v = true;
		
		// return immediately when the nodes were found
		if(found_u && found_v) return;
		
		for(Node current: root.children()){
			if(current != null){
				i++;
				
				traverseTree(current);
				i--;
				
				// write if node not already reached
				if(!found_u) path_u[i] = root.id;
				if(!found_v) path_v[i] = root.id;
				
				// determine whether to write
				if(root.id == u)found_u = true;
				if(root.id == v)found_v = true;
				
				// return immediately when the nodes were found
				if(found_u && found_v) return;
			}
		}
	}

}
