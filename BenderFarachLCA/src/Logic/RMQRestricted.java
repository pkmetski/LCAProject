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
	private int[][][] blocksRmq;

	public RMQRestricted(Map<INode, Integer> R, INode[] E, int[] L) {
		this.R = R;
		this.E = E;
		this.L = L;
	}

	public void process() {
		blockSize = (int) (Math.ceil((Math.log(L.length) / Math.log(2))) / 2);
		int blocksCount = (int) Math.ceil(L.length / (double) blockSize);
		Ap = new int[blocksCount];
		B = new int[blocksCount];
		blocksRmq = new int[blocksCount][blockSize][blockSize];

		preprocessAPrime(L, Ap, B, blockSize);
		normaliseBlocks(L, blockSize);// IS THIS NEEDED AT ALL?
		normalisePlusMinus1(L);
		preprocessUniqueBlocks(L, blocksRmq, blockSize);

		int j = 0;
		j++;
	}

	private void preprocessAPrime(int[] A, int[] Ap, int[] B, int blockSize) {

		for (int i = 0, k = 0; i < A.length; i += blockSize, k++) {
			Ap[k] = Integer.MAX_VALUE;
			for (int j = i; j < i + blockSize && j < A.length; j++) {
				if (A[j] < Ap[k]) {
					Ap[k] = A[j];
					B[k] = j;
				}
			}
		}
	}

	// normalize L by subtracting the initial offset of every block
	private void normaliseBlocks(int[] A, int blockSize) {

		int blockOffset = 0;
		for (int i = 0; i < A.length; i += blockSize) {
			// get the initial offset of the block
			blockOffset = A[i];
			for (int j = i; j < i + blockSize && j < A.length; j++) {
				// for each element in the block, subtract the initial value
				A[j] = A[j] - blockOffset;
			}
		}
	}

	// normalize A by the +-1 rule
	private void normalisePlusMinus1(int[] A) {

		for (int i = 1; i < A.length - 1; i++) {
			A[i] = A[i] - A[i + 1];
		}
	}

	private void preprocessUniqueBlocks(int[] A, int[][][] blocksRmq,
			int blockSize) {

		// keeps a collection of blocks, where first dimension specifies the
		// block signature, the second - the start index of each block in L
		int[][] uniqueBlocks = new int[(int) (Math.ceil(Math.sqrt(A.length)))][(int) (Math
				.ceil(Math.sqrt(A.length)) + 1)];

		char[] binaryKey = new char[blockSize];

		for (int i = 0; i < A.length - 1; i += blockSize) {

			for (int j = i, k = 0; j < i + blockSize && j < A.length - 1; j++) {
				// if L[i] is 1, add 1 to the
				// key, otherwise, add 0
				// (values of L should be 1
				// or -1 only)
				binaryKey[k++] = A[j] == 1 ? '1' : '0';
			}

			// after building the binary array for this block, convert it to
			// key (int)
			int key = Integer.parseInt(new String(binaryKey), blockSize);

			// get the array, corresponding to this key
			// add the start index of this block and increase the block counter
			// for this key
			uniqueBlocks[key][++uniqueBlocks[key][0]] = i;

			// if this is the first block with this key,
			// its rmqs need to be preprocessed
			if (uniqueBlocks[key][0] == 1) {
				blocksRmq[uniqueBlocks[key][1] / blockSize] = preprocessInBlockQueries(
						A, uniqueBlocks[key][1], blockSize);
			}
			// we have already computed the queries for this block type
			else if (1 < uniqueBlocks[key][0]) {
				blocksRmq[uniqueBlocks[key][uniqueBlocks[key][0]] / blockSize] = blocksRmq[uniqueBlocks[key][uniqueBlocks[key][1]]
						/ blockSize];
			}
		}
	}

	// calculate all possible RMQs in each unique block
	private int[][] preprocessInBlockQueries(int[] A, int blockStart,
			int blockSize) {

		int[][] blockRmq = new int[blockSize][blockSize];

		for (int i = blockStart; i - blockStart < blockSize && i < A.length; i++) {

			for (int j = i, minIndex = i; j - blockStart < blockSize
					&& j < A.length; j++) {

				if (A[j] < A[minIndex]) {
					minIndex = j;
				}
				blockRmq[i - blockStart][j - blockStart] = minIndex;
				blockRmq[j - blockStart][i - blockStart] = minIndex;
			}
		}
		return blockRmq;
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

		int jBStart = getBlockStart(j, L, blockSize);

		// if i and j are in the same block
		if (getBlockStart(i, L, blockSize) == jBStart) {
			// get the min for range i-j
			return getMinInBlock(i, j, L, blockSize);
			// if i and j are in different blocks
		} else {
			// get i's block end i'
			int end = getBlockEnd(i, L, blockSize);
			// get min for range i-i'
			int minI = getMinInBlock(i, end, L, blockSize);
			// get j's block start j'
			// get min for range j'-j
			int minJ = getMinInBlock(jBStart, j, L, blockSize);
			// get the min for all the blocks between i and j
			int minBetween = getMinBlocks(jBStart, end);

			return Math.min(Math.min(minI, minJ), minBetween);
		}
	}

	// i and j are known to be in the same block
	private int getMinInBlock(int i, int j, int[] A, int blockSize) {

		int bs = getBlockStart(i, A, blockSize);
		return blocksRmq[bs / blockSize][i][j];
	}

	private int getBlockStart(int j, int[] A, int blockSize) {

		// TODO: return the start of the block j belongs to
		return 0;
	}

	private int getBlockEnd(int i, int[] A, int blockSize) {

		// TODO: return the end of the block i belongs to
		return 0;
	}

	private int getMinBlocks(int start, int end) {

		// TODO: return the min index of all blocks between start and end

		// how do we do this in constant time? don't we always need some
		// iteration?
		return 0;
	}
}
