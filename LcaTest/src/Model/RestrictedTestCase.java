package Model;

import Logic.EulerTourController;
import Logic.RMQRestricted;

public class RestrictedTestCase extends TestCase {

	private EulerTourController eulerTourController;
	private RMQRestricted rmqRestricted;

	public RestrictedTestCase(Tree tree, INode node1, INode node2,
			boolean preprocessAlways) {
		super(tree, node1, node2, preprocessAlways);
		eulerTourController = new EulerTourController(tree);
	}

	public void preprocess() {
		startStopwatch();
		eulerTourController.preprocess();
		rmqRestricted = new RMQRestricted(eulerTourController.getR(),
				eulerTourController.getE(), eulerTourController.getL());
		rmqRestricted.preprocess();
		stopStopwatch();
	}

	public void executeQuery() {
		startStopwatch();
		rmqRestricted.RMQ(this.node1, this.node2);
		stopStopwatch();
	}

	@Override
	protected String getAlgorithmName() {
		return "Restricted";
	}
}
