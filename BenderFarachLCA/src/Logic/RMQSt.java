package Logic;

import java.util.Map;
import Model.INode;

// RMQ using sparse table <O(nlogn), O(1)>
public class RMQSt {

	private Map<INode, Integer> R;
	private int[][] M;
	private int[] L;
	private INode[] E;

	public RMQSt(Map<INode, Integer> R, INode[] E, int[] L) {
		this.R = R;
		this.E = E;
		this.L = L;
	}

	public void process() {

		M = new int[L.length][(int) Math.round((Math.log(L.length) / Math
				.log(2)))];

		for (int i = 0; i < L.length; i++)
			M[i][0] = i;

		for (int j = 1; (1 << j) <= L.length; j++) {
			for (int i = 0; i + (1 << j) - 1 < L.length; i++) {
				int a = i + (1 << (j - 1));
				int b = j - 1;
				if (L[M[i][j - 1]] <= L[M[i + (1 << (j - 1))][j - 1]]) {
					M[i][j] = M[i][j - 1];
				} else {
					M[i][j] = M[i + (1 << (j - 1))][j - 1];
				}
			}
		}
	}

	// returns an index in array L
	public int RMQ(INode iNode, INode jNode) {

		int i = R.get(iNode);
		int j = R.get(jNode);
		// first, make sure i<j
		if (j < i) {
			int t = j;
			j = i;
			i = t;
		} else if (i == j) {
			return -1;
		}

		int k = (int) ((Math.log(j - i)) / Math.log(2));

		if (L[M[i][k]] <= L[M[j - (1 << k) + 1][k]]) {
			return M[i][k];
		} else {
			return M[j - (1 << k) + 1][k];
		}
	}
}
