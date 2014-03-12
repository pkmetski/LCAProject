package Logic2;

import suffixtree.construction.Node;

/*****************************************************
 * Reduce the LCA problem to RMQ (perform the Euler tour) that results in 3 arrays:
 * 		E[] of nodes(their labels) with size 2*n-1;
 * 		L[] of levels(distance from the root) with size 2*n-1;
 * 		R[] of representatives' first occurrence with size n.
 * 
 * 
 * Euler tour technique:
 * http://en.wikipedia.org/wiki/Euler_tour_technique
 * 		
 *****************************************************/

public class EulerTourReduction {

	private int n;
	private Node[] E;
	private int[] L;
	private int[] R;
	private int nodeIndex = -1;
	private int level = -1;
	
	/*
	 * The list has the form of a matrix of size [2E][2],
	 * where E is the number of edges in the graph.
	 * 
	 * Different rows represent different edges,
	 * while different columns [0/1] represent first and last node respectively.
	 */
	public int[][] edge_list; // used for step1 to store a 'list' of directed edges
	
	private int cei = -1; // cei - current edge id


	public EulerTourReduction(int n) {
		this.n = n;
		E = new Node[2*n-1];
		L = new int[2*n-1];
		R = new int[n];
	}

	/*
	 * creates an edge in the edge list
	 * id -> determines the row
	 * u  -> node 1 id is stored in column 0
	 * v  -> node 2 id is stored in coulmn 1
	 */
	private void edge(int id, int u, int v){
		edge_list[id][0] = u;
		edge_list[id][1] = v;
	}
	
	public void euler_tour(Node root){
		step1(root);
		step2();
		step3();
		step4();
	}
	
	/*
	 * step 1: create a list/matrix of edges with a size 2|E| of the undirected graph G={V,E}
	 * E = V-1
	 */
	private void step1(Node root) {
		edge_list = new int[2*(root.nodes-1)][2];
		traverseTree(root);
	}
	
	/*
	 * each undirected edge is added twice as 2 directed edges
	 * 
	 * cei - current edge id
	 */
	private void traverseTree(Node parent) {
		for (Node child : parent.children()) {
			if(child!=null){
				// add the first direction going from the root to the leaves
				cei++;
				edge(cei, parent.id, child.id);
				// add the second direction going from the leaves to the root
				cei++;
				edge(cei, child.id, parent.id);
				// traverse other edges
				traverseTree(child);
			}
		}
	}
	
	/*
	 * step 2: sort the list/matrix of edges lexicographically (1st by 1st column, then by 2nd one) 
	 */
	private void step2(){
		
	}
	
	/*
	 * step 3: make adjacency lists 'next' + map to first entry 'first'
	 */
	private void step3(){
		
	}
	
	/*
	 *  step 4: make list 'succ'
	 *  this should result into the Euler route array... in our case E[i]
	 */
	private void step4(){
		
	}
	
	
	

	public int[] getR() {
		return R;
	}

	public Node[] getE() {
		return E;
	}

	public int[] getL() {
		return L;
	}
}
