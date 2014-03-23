import Logic.EulerTourController;
import Logic.RMQRestricted;
import Logic.RMQSt;
import Logic.TreeController;
import Model.INode;

public class Main {

	public static void main(String[] args) {

		TreeController treeController = new TreeController(7);
		INode tree = treeController.getTree();

		EulerTourController et = new EulerTourController(7);
		et.process(tree);

		RMQSt rmqSt = new RMQSt(et.getR(), et.getE(), et.getL());
		rmqSt.process();

		INode[] E = et.getE();

		assert rmqSt.RMQ(E[10], E[4]) == 6;
		assert rmqSt.RMQ(E[7], E[7]) == -1;
		assert rmqSt.RMQ(E[4], E[2]) == 3;
		assert rmqSt.RMQ(E[8], E[10]) == 9;
		assert rmqSt.RMQ(E[1], E[7]) == 6;

		RMQRestricted rmqRestricted = new RMQRestricted(et.getR(), et.getE(),
				et.getL());
		rmqRestricted.process();

		int i = 0;
		i++;
	}
}
