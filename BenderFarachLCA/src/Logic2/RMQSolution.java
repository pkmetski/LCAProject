package Logic2;

import java.util.Arrays;
import java.util.HashMap;

public class RMQSolution {

	private int[] e;
	private int[] l;
	private int[] r;
	private int[][] st; // sparse table
	private int a; // node id of the answer
	
	private int bs; 		// block size (logN/2)
	private int[] val; 		// values for min element in blocks of size logN/2 -> resembles A'
	private int[] pos; 		// positions for min element in blocks of size logN/2 -> resembles B
	private String[] nb; 	// normalized block reference for each block
	private HashMap<String,String> patterns = new HashMap<String,String>(); //unique normalized blocks and their O(n^2) table
	
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
	 */
	private void stepA2(int u, int v){
		// if it happens that we have i = j, return e[i] so that log 0 does not occur
		if(u == v){a = e[u]; return;}
		
		int i = (u < v)? u : v;
		int j = (u > v)? u : v;
		int k = (int) Math.round(Math.log(j-i) / Math.log(2));
		
		a = (l[st[i][k]] <= l[st[j-(1 << k)+1][k]])? e[st[i][k]] : e[st[j-(1 << k)+1][k]];
	}
	
	public void solveB(int u, int v){
		stepB1();
		stepB2();
	}
	
	/*
	 * Partition A into blocks of size (log n) / 2
	 * 
	 * determine block size
	 * define arrays for the value and position of the minimum element in a block
	 * define an array of the same size to reference normalized blocks
	 * i*bs maps val[i] to l-block
	 */
	private void stepB1(){
		double lgN = Math.round((Math.log(l.length) / Math.log(2)));
		bs = (int) (lgN / 2); // block size
		
		int vps = (int) Math.round(2*l.length / lgN);// val(A') and pos(B) size
		val = new int[vps]; 		// val(A')
		pos = new int[vps]; 		// pos(B )
		nb = new String[vps]; 	// normalized block reference
		StringBuilder vector = new StringBuilder();// vector pattern
		
		int cb = 0; 					// keeps track of current block
		int minV = l[0];			 	// keeps track of minimal value
		int minP = 0; 					// keeps track of minimal position
		
		for(int i = 1; i < l.length; i++){
			// determine the start of a new block - finish previous and reset values
			if(i % bs == 0){
				// save unique vector patterns and start a new one
				nb[cb] = vector.toString();
				if(!patterns.containsKey(vector)) patterns.put(nb[cb], "table");
				vector = new StringBuilder(); //start new
				
				val[cb]=minV;
				pos[cb]=minP;
				cb++;
				minV = Integer.MAX_VALUE;
			}
			else{
				// continue vector pattern
				vector.append(l[i-1] - l[i]);
			}
			
			// keep track of min value and its position
			if(l[i] < minV){
				minV = l[i];
				minP = i;
			}
			// add the remaining elements of the array in a separate block of a smaller size
			if(i == l.length-1){
				// save vector pattern
				if(i % bs -1 > 0){//avoid a pattern of size 0
					nb[cb] = vector.toString();
					if(!patterns.containsKey(vector)) patterns.put(nb[cb], "table");
				}
				
				val[cb]=minV;
				pos[cb]=minP;
			}
		}
	}
	
	/*
	 * Check partitioning done correctly
	 */
	public void checkPartition(){
		System.out.println();
		System.out.println(Arrays.toString(val));
		System.out.println(Arrays.toString(pos));
	}
	
	/*
	 * Check HashMap
	 * http://stackoverflow.com/questions/5920135/printing-hashmap-in-java
	 */
	public void checkHashMap(){
		System.out.println();
		for (String name: patterns.keySet()){
			String key =name;
			String value = patterns.get(name);
			System.out.println(key + " " + value);
		}
	}
	
	private void stepB2(){
		
	}
	
}
