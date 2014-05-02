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
	private int[][] M;
	private int[] Lp;

	public RMQRestricted(Map<INode, Integer> R, INode[] E, int[] L) {
		this.R = R;
		this.E = E;
		this.L = L;
	}

	public void preprocess() {
		blockSize = (int) (Math.ceil((Math.log(L.length) / Math.log(2))) / 2);
		int blocksCount = (int) Math.ceil(L.length / (double) blockSize);
		Ap = new int[blocksCount];
		B = new int[blocksCount];
		// the preprocessed rmqs for each block
		blocksRmq = new int[blocksCount][blockSize][blockSize];
		Lp = new int[L.length];

		M = new int[Ap.length][(int) Math.round((Math.log(Ap.length) / Math
				.log(2)))];

		populateAPrime(L, Ap, B, blockSize);
		preprocessAPrime(Ap, M);
		normaliseBlocks(L, Lp, blockSize);
		normalisePlusMinus1(Lp);
		preprocessUniqueBlocks(Lp, blocksRmq, blockSize);
	}

	private void populateAPrime(int[] A, int[] Ap, int[] B, int blockSize) {

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

	private void preprocessAPrime(int[] Ap, int[][] M) {

		// preprocess A' with ST algorithm

		for (int i = 0; i < Ap.length; i++)
			M[i][0] = i;

		for (int j = 1; (1 << j) <= Ap.length; j++)
			for (int i = 0; i + (1 << j) - 1 < Ap.length; i++)
				if (Ap[M[i][j - 1]] <= Ap[M[i + (1 << (j - 1))][j - 1]]) {
					M[i][j] = M[i][j - 1];
				} else {
					M[i][j] = M[i + (1 << (j - 1))][j - 1];
				}
	}

	// normalize L by subtracting the initial offset of every block
	private void normaliseBlocks(int[] A, int[] Lp, int blockSize) {

		int blockOffset = 0;
		for (int i = 0; i < A.length; i += blockSize) {
			// get the initial offset of the block
			blockOffset = A[i];
			for (int j = i; j < i + blockSize && j < A.length; j++) {
				// for each element in the block, subtract the initial value
				Lp[j] = A[j] - blockOffset;
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

			// in the array, corresponding to this key
			// add the start index of this block and increase the block counter
			uniqueBlocks[key][++uniqueBlocks[key][0]] = i;

			// if this is the first block with this key,
			// its rmqs need to be preprocessed
			if (uniqueBlocks[key][0] == 1) {

				blocksRmq[uniqueBlocks[key][1] / blockSize] = preprocessInBlockQueries(
						A, uniqueBlocks[key][1], blockSize);
			}
			// we have already computed the queries for this block type
			else if (1 < uniqueBlocks[key][0]) {

				blocksRmq[uniqueBlocks[key][uniqueBlocks[key][0]] / blockSize] = blocksRmq[uniqueBlocks[key][1]
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
				blockRmq[i - blockStart][j - blockStart] = minIndex
						- blockStart;
				blockRmq[j - blockStart][i - blockStart] = minIndex
						- blockStart;
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

		int iBStart = getBlockStart(i, blockSize);
		int jBStart = getBlockStart(j, blockSize);

		// if i and j are in the same block
		if (iBStart == jBStart) {
			// get the min for range i-j
			return getMinInBlock(i, j, blockSize);
			// if i and j are in different blocks
		} else {
			// get i's block end i'
			int iBEnd = getBlockEnd(i, blockSize);
			// get min for range i-i'
			int minI = getMinInBlock(i, iBEnd, blockSize);
			// get j's block start j'
			// get min for range j'-j
			int minJ = getMinInBlock(jBStart, j, blockSize);

			int minIndex;
			if (L[minI] < L[minJ]) {
				minIndex = minI;
			} else {
				minIndex = minJ;
			}

			// get the min for all the blocks between i and j
			int minBetween = getMinBlocks(iBEnd + 1, jBStart - 1, blockSize);
			if (-1 < minBetween) {
				if (L[minBetween] < L[minIndex]) {
					minIndex = minBetween;
				}
			}
			return minIndex;
		}
	}

	// i and j are known to be in the same block
	private int getMinInBlock(int i, int j, int blockSize) {

		int blockStart = getBlockStart(i, blockSize);
		return blocksRmq[blockStart / blockSize][i - i][j - i] + i;
	}

	private int getBlockStart(int j, int blockSize) {

		return (j / blockSize) * blockSize;
	}

	private int getBlockEnd(int i, int blockSize) {

		return getBlockStart(i, blockSize) + blockSize - 1;
	}

	private int getMinBlocks(int i, int j, int blockSize) {

		i /= blockSize;
		j /= blockSize;
		if (j < i) {
			return -1;
		}
		int k = (int) ((Math.log(j - i)) / Math.log(2));

		if (Ap[M[i][k]] <= Ap[M[j - (1 << k) + 1][k]]) {
			return B[M[i][k]];
		} else {
			return B[M[j - (1 << k) + 1][k]];
		}
	}
}
