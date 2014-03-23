package Logic2;

import java.util.Arrays;
import java.util.HashMap;

public class RMQSolution {

	private int[] e;
	private int[] l;
	private int[] r;
	private int[][] st; // sparse table
	private int[][] st2;// sparse table for the +/-1 RMQ partitioning
	private int a; // node id of the answer
	
	private int bs; 		// block size (logN/2)
	private int[] val; 		// values for min element in blocks of size logN/2 -> resembles A'
	private int[] pos; 		// positions for min element in blocks of size logN/2 -> resembles B
	private String[] nb; 	// normalized block reference for each block
	private HashMap<String,int[][]> patterns = new HashMap<String,int[][]>(); //unique normalized blocks and their O(n^2) table
	
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
	 * Pre-processing
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
	 * Querying
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
	
	public int solveB(int u, int v){
		stepB1();
		stepB2();
		stepB3(r[u],r[v]);
		return a;
	}
	
	/*
	 * Pre-processing 1
	 * Partition A (l) into blocks of size (log n) / 2
	 * - determine block size
	 * - define arrays for the value and position of the minimum element in a block
	 * - define an array of the same size to reference normalized blocks
	 * - create and save in-block-tables while discovering blocks and unique vectors
	 * 
	 * i*bs maps val[i] to A-block
	 */
	private void stepB1(){
		double lgN = Math.round((Math.log(l.length) / Math.log(2))); // logN
		bs = (int) (lgN / 2); // block size
		
		int vps = (int) Math.round(2*l.length / lgN);// val(A') and pos(B) size
		val = new int[vps]; 		// val(A')
		pos = new int[vps]; 		// pos(B )
		nb = new String[vps]; 		// normalized block reference
		
		StringBuilder vector = new StringBuilder();	// keeps track of vector pattern
		int cb = 0; 								// keeps track of current block
		int minV = l[0];			 				// keeps track of minimal value
		int minP = 0; 								// keeps track of minimal position
		
		for(int i = 1; i < l.length; i++){
			
			// determine the start of a new block - finish previous and reset values
			if(i % bs == 0){
				
				//// check block
				//System.out.println();
				//for(int x = 0 + cb*bs; x < bs + cb*bs; x++){
				//	System.out.print(""+l[x]+",");
				//}
				
				// make reference to normalized block
				nb[cb] = vector.toString();
				
				// save unique vector patterns
				if(!patterns.containsKey(vector.toString())){
					// create O(n^2) position table
					int[][] in_block_table = new int[bs][bs];
					for(int x = 0; x < bs; x++)
						for(int y = x; y < bs; y++){
							if(x == y){
								in_block_table[x][y] = x;
								continue;
							}
							else{
								if(l[y + cb*bs] < l[in_block_table[x][y-1]  + cb*bs]){  // remember indent of block
									in_block_table[x][y] = y;
									in_block_table[y][x] = y;
								}
								else{
									in_block_table[x][y] = in_block_table[x][y-1];
									in_block_table[y][x] = in_block_table[x][y-1];
								}
							}
						}
					
					//// check vector
					//System.out.println();
					//System.out.print(vector);
					
					//// check table
					//System.out.println();
					//for (int[] row : in_block_table)
				    //    System.out.println(Arrays.toString(row));
					
					// save pattern and table
					patterns.put(nb[cb], in_block_table);
				}
				
				// start a new vector pattern
				vector = new StringBuilder();
				
				// determine minimum for block
				val[cb]=minV;
				pos[cb]=minP;
				
				// mark the beginning of a new block
				cb++;
				
				// reset minimum
				minV = Integer.MAX_VALUE;
			}
			else{
				// continue vector pattern
				vector.append(l[i] - l[i-1]);
			}
			
			// keep track of min value and its position
			if(l[i] < minV){
				minV = l[i];
				minP = i;
			}
			
			// add the remaining elements of the array in a separate block of a smaller size
			if(i == l.length-1){
				
				//// check block
				//System.out.println();
				//for(int x = 0 + cb*bs; x < l.length; x++){
				//	System.out.print(""+l[x]+",");
				//}
				
				// avoid a pattern of size 0
				if(i % bs -1 > 0){
					
					// make reference to normalized block
					nb[cb] = vector.toString();
					
					// save unique vector pattern
					if(!patterns.containsKey(vector)){
						// create O(n^2) position table
						int[][] in_block_table = new int[l.length-cb*bs][l.length-cb*bs];
						for(int x = 0; x < l.length-cb*bs; x++)
							for(int y = x; y < l.length-cb*bs; y++){
								if(x == y){
									in_block_table[x][y] = x;
									continue;
								}
								else{
									if(l[y + cb*bs] < l[in_block_table[x][y-1]  + cb*bs]){  // remember indent of block
										in_block_table[x][y] = y;
										in_block_table[y][x] = y;
									}
									else{
										in_block_table[x][y] = in_block_table[x][y-1];
										in_block_table[y][x] = in_block_table[x][y-1];
									}
								}
							}

						
						// save pattern and table
						patterns.put(nb[cb], in_block_table);
					}
				}
				
				// determine minimum for last block
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
			int[][] in_block_table = patterns.get(name);
			// check table
			System.out.println(key);
			for (int[] row : in_block_table)
		        System.out.println(Arrays.toString(row));
		}
	}
	
	/*
	 * Pre-processing 2
	 * Use RMQ solveA (only the pre-processing 'stepA1') for the partitioned blocks
	 * (can only be done after partitioning)
	 * 
	 * create a sparse table for each possible query
	 * 
	 * pre-compute query of a length power of 2:
	 * st2[i,j] = {st2[i,j-1] if l[st2[i,j-1]] <= l[st2[i+2^(j-1)-1,j-1]] | st2[i+2^(j-1)-1,j-1] otherwise}
	 */
	private void stepB2(){
		st2 = new int[val.length][(int) Math.round((Math.log(val.length) / Math.log(2))) + 1];
		
		// initialize sparse table for the intervals with length 1
		for (int i = 0; i < val.length; i++)
			st2[i][0] = i;
		
		// compute values from smaller to bigger intervals
		for (int j = 1; j < st2[0].length; j++)
			for (int i = 0; i + (1 << j) - 1 < st2.length; i++)
				if (val[st2[i][j - 1]] <= val[st2[i + (1 << (j - 1))][j - 1]])
					st2[i][j] = st2[i][j - 1];
				else
					st2[i][j] = st2[i + (1 << (j - 1))][j - 1];
	}
	
	/*
	 * Check sparse table2 filled correctly
	 */
	public void checkSparseTree2(){
		System.out.println();
		for (int[] row : st2) 
	        System.out.println(Arrays.toString(row));
	}
	
	/*
	 * Querying
	 * - determine whether i and j are in the same block
	 * - determine whether there are blocks between i and j
	 * 
	 */
	private void stepB3(int u, int v){
		
		if(u == v){a = e[u]; return;}
		
		int i = (u < v)? u : v;
		int j = (u > v)? u : v;
		
		//i is in block (blocks start from 0):
		int i_block = i/bs;
		// at position:
		int i_pos = i%bs;
		
		//j is in block (blocks start from 0):
		int j_block = j/bs;
		// at position:
		int j_pos = j%bs;
		
		//blocks between i_block and j_block
		int between_blocks = j_block - i_block - 1;
		
		if(i_block == j_block){
			// find normalized block table
			String vector_pattern = nb[i_block];
			int[][] in_block_table = patterns.get(vector_pattern);
			a = e[in_block_table[i_pos][j_pos]+i_block*bs];
		}
		else{
			if(between_blocks > 0){
				
				// find normalized block table of i
				String vector_pattern_i = nb[i_block];
				int[][] in_block_table_i = patterns.get(vector_pattern_i);
				int min_i_pos = in_block_table_i[i_pos][in_block_table_i.length-1];
				
				// find normalized block table of j
				String vector_pattern_j = nb[j_block];
				int[][] in_block_table_j = patterns.get(vector_pattern_j);
				int min_j_pos = in_block_table_j[0][j_pos];
				
				// between blocks querying (actual position in l)
				int min_between = 0;
				
				// if it happens that we have 1 in-between block, return its result so that log 0 does not occur
				if(between_blocks == 1){
					min_between = pos[i_block+1];
				}
				else{
					int x = i_block+1; // after i_block
					int y = j_block-1; // before j_block
					
					int k = (int) Math.round(Math.log(y-x) / Math.log(2));
					
					min_between = (val[st2[x][k]] <= val[st2[y-(1 << k)+1][k]])? pos[st2[x][k]] : pos[st2[y-(1 << k)+1][k]];
				}
				
				if(l[min_i_pos + i_block*bs] <= l[min_j_pos + j_block*bs]){
					if(l[min_i_pos + i_block*bs] <= l[min_between]){
						a = e[min_i_pos + i_block*bs];
					}
					else{
						a = e[min_between];
					}
				}
				else{
					if(l[min_between] <= l[min_j_pos + j_block*bs]){
						a = e[min_between];
					}
					else{
						a = e[min_j_pos + j_block*bs];
					}
				}
			}
			else{
				// find normalized block table of i
				String vector_pattern_i = nb[i_block];
				int[][] in_block_table_i = patterns.get(vector_pattern_i);
				int min_i_pos = in_block_table_i[i_pos][in_block_table_i.length-1];
				
				// find normalized block table of j
				String vector_pattern_j = nb[j_block];
				int[][] in_block_table_j = patterns.get(vector_pattern_j);
				int min_j_pos = in_block_table_j[0][j_pos];
				
				if(l[min_i_pos + i_block*bs] <= l[min_j_pos + j_block*bs]){
					a = e[min_i_pos + i_block*bs];
				}
				else{
					a = e[min_j_pos + j_block*bs];
				}
			}
		}
	}
}
