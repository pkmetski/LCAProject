package Model;

import Logic.LCANaive;

public class NaiveTestCase extends TestCase {

	private LCANaive naiveAlgorithm;

	public NaiveTestCase(Tree tree, INode node1, INode node2,
			boolean preprocessAlways) {
		super(tree, node1, node2, preprocessAlways);

		naiveAlgorithm = new LCANaive(tree);
	}

	public void preprocess() {
		// do nothing, naive doesn't preprocess
	}

	public void executeQuery() {
		startStopwatch();
		naiveAlgorithm.LCA(this.node1, this.node2);
		stopStopwatch();
	}

	@Override
	protected String getAlgorithmName() {
		return "Naive";
	}
}
