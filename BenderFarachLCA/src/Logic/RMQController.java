package Logic;

import java.util.Map;

import Model.INode;

public class RMQController {

	private Map<INode, Integer> R;
	private int[][] M;
	private int[] L;
	private INode[] E;

	// RMQ using sparse tree
	public RMQController() {

	}

	public void process(Map<INode, Integer> R, int[] L, INode[] E) {
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

	public INode RMQ(INode u, INode v) {

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
