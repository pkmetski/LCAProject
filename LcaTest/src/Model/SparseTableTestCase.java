package Model;

import Logic.EulerTourController;
import Logic.RMQSt;

public class SparseTableTestCase extends TestCase {

	private EulerTourController eulerTourController;
	private RMQSt rmqSparseTable;

	public SparseTableTestCase(Tree tree, INode node1, INode node2,
			boolean preprocessAlways) {
		super(tree, node1, node2, preprocessAlways);
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

	public void executeQuery() {
		startStopwatch();
		rmqSparseTable.RMQ(this.node1, this.node2);
		stopStopwatch();
	}

	@Override
	protected String getAlgorithmName() {
		return "Sparse Table";
	}
}
