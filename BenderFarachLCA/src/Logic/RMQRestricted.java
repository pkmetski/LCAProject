package Logic;

import java.util.Map;

import Model.INode;

//RMQ for the +-1 restricted version of the algorithm <O(n), O(1)>
public class RMQRestricted {

	private Map<INode, Integer> R;
	private int[] L;
	private INode[] E;

	private int[] Ap;
	private int[] B;
	private int blockSize;
	private int[][] blocks;

	public RMQRestricted(Map<INode, Integer> R, INode[] E, int[] L) {
		this.R = R;
		this.E = E;
		this.L = L;
	}

	public void process() {
		blockSize = (int) (Math.ceil((Math.log(L.length) / Math.log(2))) / 2);
		int length = (int) Math.ceil(L.length / (double) blockSize);
		Ap = new int[length];
		B = new int[length];

		// 1. preprocess A'
		// initialise A'
		for (int i = 0; i < Ap.length; i++) {
			Ap[i] = Integer.MAX_VALUE;
		}

		for (int i = 0; i < Ap.length; i++) {
			Ap[i] = i;
			B[i] = i;
		}

		for (int i = 0, k = 0; i < L.length; i += blockSize, k++) {
			for (int j = i; j < i + blockSize && j < L.length; j++) {
				if (L[i] < Ap[k]) {
					Ap[k] = L[i];
					B[k] = i;
				}
				// normalize blocks
				// L[j] = L[j] - L[i];
			}
		}

		for (int i = 1; i < L.length - 1; i++) {
			L[i] = L[i] - L[i + 1];
		}

		blocks = new int[(int) (Math.ceil(Math.sqrt(L.length)))][(int) (Math
				.ceil(Math.sqrt(L.length)) + 1)];// keeps a collection of
													// blocks, where
													// first
													// dimension specifies the
													// block
													// signature,
													// the second - the start
													// index of each
													// block in L

		char[] key = new char[blockSize];

		// 3. calculate all possible RMQs in each unique block
		for (int i = 0; i < L.length - 1; i += blockSize) {
			if (i == 0) {
				continue;
			}
			for (int j = i, k = 0; j < i + blockSize && j < L.length - 1; j++) {
				// if L[i] is 1, add 1 to the
				// key, otherwise, add 0
				// (values of L should be 1
				// or -1 only)
				key[k++] = L[i] == 1 ? '1' : '0';
			}

			// after building the binary array for this block, convert it to
			// key (int)
			int intKey = Integer.parseInt(new String(key), blockSize);

			// get the array, corresponding to this key
			// add the beginning of this block and increase the block counter
			// for this key
			blocks[intKey][++blocks[intKey][0]] = i;

			// if there is just 1 block with this key, it needs to be
			// preprocessed
			if (blocks[intKey][blocks[intKey][0]] == 1) {
				// TODO: do the preprocessing of the block here
			}
		}
	}

	public int RMQ(INode iNode, INode jNode) {
		int i = R.get(iNode);
		int j = R.get(jNode);

		if (j < i) {
			int t = j;
			j = i;
			i = t;
		} else if (i == j) {
			return -1;
		}

		// TODO: determining which block index i belongs to

		int iBStart = getBlockStart(i, L, blockSize);
		int jBStart = getBlockStart(j, L, blockSize);

		// if i and j are in the same block
		if (iBStart == jBStart) {
			// get the min for range i-j
			return getMinWithinBlock(i, j);
		} else {
			// if i and j are in different blocks
			// get i's block end i'
			// get min for range i-i'
			// get j's block start j'
			// get min for range j'-j
			// get the min for all the blocks between i and j
		}
		return 0;
	}

	private int getMinWithinBlock(int i, int j) {
		return 0;
	}

	private int getBlockStart(int i, int[] A, int blockSize) {

		return 0;
	}
}
