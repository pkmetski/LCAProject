package Model;

import Logic.LCANaive;

public class NaiveTestCase extends TestCase {

	private LCANaive naiveAlgorithm;

	public NaiveTestCase(Tree tree, boolean preprocessAlways) {
		super(tree, preprocessAlways);

		naiveAlgorithm = new LCANaive(tree);
	}

	public void preprocess() {
		// do nothing, naive doesn't preprocess
	}

	public void executeQuery(INode node1, INode node2) {
		startStopwatch();
		naiveAlgorithm.LCA(node1, node2);
		stopStopwatch();
	}

	@Override
	protected String getAlgorithmName() {
		return "Naive";
	}
}
