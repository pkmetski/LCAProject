package Model;

import Logic.EulerTourController;
import Logic.RMQSt;

public class SparseTableTestCase extends TestCase {

	private EulerTourController eulerTourController;
	private RMQSt rmqSparseTable;

	public SparseTableTestCase(Tree tree, boolean preprocessAlways) {
		super(tree, preprocessAlways);
		eulerTourController = new EulerTourController(tree);
	}

	public void preprocess() {
		startStopwatch();
		eulerTourController.preprocess();
		rmqSparseTable = new RMQSt(eulerTourController.getR(),
				eulerTourController.getE(), eulerTourController.getL());
		rmqSparseTable.preprocess();
		stopStopwatch();
	}

	public void executeQuery(INode node1, INode node2) {
		startStopwatch();
		rmqSparseTable.RMQ(node1, node2);
		stopStopwatch();
	}

	@Override
	protected String getAlgorithmName() {
		return "Sparse Table";
	}
}
