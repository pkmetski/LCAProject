package Logic2;

import java.util.Arrays;
import java.util.Map;

import suffixtree.construction.Node;

public class RMQSolution {

	private Map<Node, Integer> R;
	private int[][] M;
	private int[] L;
	private Node[] E;
	
	private int[] e;
	private int[] l;
	private int[] r;
	private int[][] st;
	private int a; // node id of the answer

	// RMQ using sparse tree
	public RMQSolution(int[] e, int[] l, int[] r){
		this.e = e;
		this.l = l;
		this.r = r;
	}
	
	/*
	 * select i and j based on their first occurrence (from r)
	 */
	public int solveA(int u, int v){
		stepA1();
		stepA2(r[u],r[v]);
		return a;
	}
	
	/*
	 * create a sparse table for each possible query
	 * 
	 * pre-compute query of a length power of 2:
	 * st[i,j] = {st[i,j-1] if l[st[i,j-1]] <= st[i+2^(j-1)-1,j-1] | st[i+2^(j-1)-1,j-1] otherwise}
	 */
	private void stepA1(){
		st = new int[l.length][(int) Math.round((Math.log(l.length) / Math.log(2))) + 1];
		
		// initialize sparse table for the intervals with length 1
		for (int i = 0; i < l.length; i++)
			st[i][0] = i;
		
		// compute values from smaller to bigger intervals
		for (int j = 1; j < st[0].length; j++)
			for (int i = 0; i + (1 << j) - 1 < st.length; i++)
				if (l[st[i][j - 1]] <= l[st[i + (1 << (j - 1))][j - 1]]) {
							st[i][j] = st[i][j - 1];
						} else {
							st[i][j] = st[i + (1 << (j - 1))][j - 1];
						}
	}
	
	/*
	 * Check sparse table filled correctly
	 */
	public void checkSparseTree(){
		for (int[] row : st) 
	        System.out.println(Arrays.toString(row));
	}
	
	/*
	 * select 2 overlapping blocks that cover the subrange entirely
	 * 
	 * 
	 */
	private void stepA2(int u, int v){
		// if it happens that we have i = j, return e[i] so that log 0 does not occur
		if(u == v){a = e[u]; return;}
		
		int i = (u < v)? u : v;
		int j = (u > v)? u : v;
		int k = (int) Math.round(Math.log(j-i) / Math.log(2));
		
		a = Math.min(l[st[i][k]], l[st[j-(2 << k)+1][k]]);
	}
	
	public void solveB(int u, int v){
		stepB1();
		stepB2();
	}
	
	private void stepB1(){
		
	}
	
	private void stepB2(){
		
	}
	
	public void process(Map<Node, Integer> R, int[] L, Node[] E) {
		int i, j;
		this.R = R;
		this.L = L;
		this.E = E;
		this.M = new int[R.size()][(int) Math.round((Math
				.log(R.keySet().size()) / Math.log(2))) + 1];

		// initialize M for the intervals with length 1
		for (i = 0; i < R.keySet().size(); i++)
			M[i][0] = i;

		// compute values from smaller to bigger intervals
		for (j = 1; (1 << j) <= R.keySet().size(); j++)
			for (i = 0; i + (1 << j) - 1 < R.keySet().size(); i++)
				if (L[R.get(E[M[i][j - 1]])] < L[R
						.get(E[M[i + (1 << (j - 1))][j - 1]])]) {
					M[i][j] = M[i][j - 1];
				} else {
					M[i][j] = M[i + (1 << (j - 1))][j - 1];
				}

		// compare nodes levels
		// get node level from L (needs node index)
		// get node index (needs node and R)
		// get node (needs index and E)
		// get index (needs M)
	}
	
	public Node RMQ(Node u, Node v) {

		int k = (int) (Math.log(Math.abs(R.get(v) - R.get(u))) / Math.log(2));

		// do we need the abs?
		// what about identifying the bigger (and smaller) index in the
		// beginning?
		if (L[M[R.get(u)][k]] <= L[M[(int) (R.get(v) - 1 << k + 1)][k]]) {
			return E[M[R.get(u)][k]];
		} else {
			return E[M[(int) (R.get(v) - 1 << k + 1)][k]];
		}
	}
}
