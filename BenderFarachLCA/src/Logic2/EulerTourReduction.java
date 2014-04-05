package Logic2;

import java.util.Arrays;

import suffixtree2.construction.Node;

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

	private int n; // number of vertices |V|
	private int[] E;
	private int[] L;
	private int[] R;
	
	/*
	 * The list has the form of a matrix of size [2E][2],
	 * where E is the number of edges in the graph.
	 * 
	 * Different rows represent different edges,
	 * while different columns [0/1] represent first and last node respectively.
	 */
	public int[][] edge_list; // used for step1 to store a 'list' of directed edges
	
	private int cni = -1; // cni - current node id
	private int cei = -1; // cei - current edge id
	
	private EdgesOfNode[] next; // adjacency lists for each node of size [V]
	private int[] first; // map to the first entry of the adjacency list of size [V]
	
	private int ci; // current index from the tour
	private int lvl; // current level in the tour

	public EulerTourReduction(int n) {
		this.n = n;
		E = new int[2*n-1];
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
	
	public void euler_tour_plamen_jesper(Node root){
		E[ci] = root.id;
		L[ci] = lvl;
		R[root.id] = ci;
		ci++;
		for(Node current: root.children()){
			if(current != null){
				lvl++;
				euler_tour_plamen_jesper(current);
				lvl--;
				E[ci] = root.id;
				L[ci] = lvl;
				if(root.id!=0 && R[root.id]==0){
					R[root.id] = ci;
				}
				ci++;
			}
		}
	}
	
	/*
	 * step 1: create a list/matrix of edges with a size 2|E| of the undirected graph G={V,E}
	 * E = V-1
	 */
	private void step1(Node root) {
		edge_list = new int[2*(root.nodes-1)][2];
		cni++; root.id = cni; // re-write the node ids
		traverseTree(root);
	}
	
	/*
	 * each undirected edge is added twice as 2 directed edges
	 * 
	 * cni - current node id
	 * cei - current edge id
	 */
	private void traverseTree(Node parent) {
		for (Node child : parent.children()) {
			if(child!=null){
				cni++; child.id = cni; // re-write the node ids
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
	 * 
	 * using java's built-in sort and extension for the situation
	 * http://stackoverflow.com/questions/15452429/java-arrays-sort-2d-array
	 */
	private void step2(){
		Arrays.sort(edge_list,new java.util.Comparator<int[]>() {
		    public int compare(int[] a, int[] b) {
		        return Integer.compare(a[0], b[0]);
		    }
		});
	}
	
	/*
	 * step 3: make adjacency lists 'next' + map to first entry 'first'
	 * 
	 * if(x!=u) => first(u)=(u,v)
	 * if(x==u) => next(x,y)=(u,v)
	 */
	private void step3(){
		next = new EdgesOfNode[n];
		first = new int[n];
		
		int[] prev_edge = edge_list[0]; // (x,y)
		for(int[] e: edge_list){ // (u,v)
			// ... only map the first entry of the adjacent edges sequence
			if(prev_edge==edge_list[0]) first[prev_edge[0]] = prev_edge[1]; // special case - map the root to the first child for a circular tour
			if(e[0]!=prev_edge[0]){
				first[e[0]] = e[1];
			}
			// ... add edge to adjacency list of current node
			if(next[e[0]]==null) next[e[0]]=new EdgesOfNode();
			if(e[0]==prev_edge[0]){
				next[e[0]].addAdjacent(e);
			}
			
			// move previous edge (x,y) one place before the current edge (u,v)
			//... for each other than the first edge in the list make the previous edge the current one
			if(e!=edge_list[0]){
				prev_edge = e;
			}
		}
	}
	
	/*
	 *  step 4: make list 'succ'
	 *  this should result into the Euler route array... in our case E[i]
	 */
	private void step4(){
		// instead of making a list of edges 'succ' use node visiting instead (E[i])
		int i = 0; //pointer to the E[i] currently overwriting... tour will be finished when all of E is populated
		int lvl = 0;
		int cn = next[0].getAdjacents().get(0)[0];//current node starts from the first node from the first edge from the first adjacency list (root node)
		E[i] = cn; //add root node
		L[i] = lvl;
		R[i] = cn;
		i++;
		
		int ni; //next index
		for(;i<E.length;i++){
			if(next[cn].next < next[cn].getAdjacents().size()){ //follow nodes down to leaves
				ni = next[cn].next;
				next[cn].next++;
				cn = next[cn].getAdjacents().get(ni)[1]; //always remove the first of the adjacent edges... i.e. the next one
				lvl++;
				if(cn!=0 && R[cn]==0){
					R[cn] = i;
				}
			}
			else{ //backtrack to parent -> follow mapping
				cn = first[cn];
				lvl--;
			}
			E[i] = cn;
			L[i] = lvl;
		}
	}
	
	/*
	 * Check adjacency lists filled correctly (next)
	 */
	public void checkAdjacency(){
		for(EdgesOfNode v: next){
			if(!v.getAdjacents().isEmpty()){
				System.out.println("adjacency list for node "+v.getAdjacents().get(0)[0]);
				for(int[] e: v.getAdjacents()){
					System.out.println(""+e[0]+","+e[1]);
					}
			}
		}
	}
	
	/*
	 * Check mapping filled correctly (first)
	 */
	public void checkMapping(){
		for(int i=0; i<first.length; i++){
			System.out.println(""+i+"->"+first[i]);
		}
	}
	
	public void checkE(){
		for(int i=0; i<E.length; i++)
			System.out.print(""+E[i]+",");
	}
	
	public void checkL(){
		for(int i=0; i<L.length; i++)
			System.out.print(""+L[i]+",");
	}

	public void checkR(){
		for(int i=0; i<R.length; i++)
			System.out.print(""+R[i]+",");
	}
	
	public int[] getR() {
		return R;
	}

	public int[] getE() {
		return E;
	}

	public int[] getL() {
		return L;
	}
}
