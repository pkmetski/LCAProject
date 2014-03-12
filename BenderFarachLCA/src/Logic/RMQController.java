package Logic;

import java.util.Map;

import Model.INode;

public class RMQController {

	private Map<INode, Integer> R;
	private int[][] M;
	private int[] L;
	private INode[] E;

	// RMQ using sparse table <O(nlogn), O(1)>
	public RMQController() {

	}

	public void process(Map<INode, Integer> R, INode[] E, int[] L) {
		this.R = R;
		this.E = E;
		this.L = L;
		this.M = new int[E.length][(int) Math.round((Math.log(E.length) / Math
				.log(2)))];

		// initialise M for the intervals with length 1
		for (int i = 0; i < E.length; i++)
			M[i][0] = i;

		// compute values from smaller to bigger intervals
		for (int j = 1; (1 << j) <= E.length; j++)
			for (int i = 0; i + (1 << j) - 1 < E.length; i++)
				if (L[R.get(E[M[i][j - 1]])] <= L[R
						.get(E[M[i + (1 << (j - 1))][j - 1]])]) {/* deleted "-1" */
					M[i][j] = M[i][j - 1];
				} else {
					M[i][j] = M[i + (1 << (j - 1))][j - 1];
				}
	}

	// returns an index in array E
	public int RMQ(int i, int j) {
		int k = (int) ((Math.log(j - i)) / Math.log(2));

		if (L[R.get(E[M[i][k]])] <= L[R.get(E[M[j - (1 << k) + 1][k]])]) {
			return M[i][k];
		} else {
			return M[j - (1 << k) + 1][k];
		}
	}
}
