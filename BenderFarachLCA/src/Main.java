import Logic.EulerTourController;
import Logic.LCANaive;
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
		INode[] E = et.getE();

		/* Sparse table algorithm <O(nlogn), 1> */
		RMQSt rmqSt = new RMQSt(et.getR(), et.getE(), et.getL());
		rmqSt.process();

		assert rmqSt.RMQ(E[10], E[4]) == 6;
		assert rmqSt.RMQ(E[7], E[7]) == -1;
		assert rmqSt.RMQ(E[4], E[2]) == 3;
		assert rmqSt.RMQ(E[8], E[10]) == 9;
		assert rmqSt.RMQ(E[1], E[7]) == 6;

		/* Restricted algorithm <O(n), 1> */
		RMQRestricted rmqRestricted = new RMQRestricted(et.getR(), et.getE(),
				et.getL());
		rmqRestricted.process();

		assert rmqRestricted.RMQ(E[10], E[4]) == 6;
		assert rmqRestricted.RMQ(E[7], E[7]) == -1;
		assert rmqRestricted.RMQ(E[4], E[2]) == 3;
		assert rmqRestricted.RMQ(E[8], E[10]) == 9;
		assert rmqRestricted.RMQ(E[1], E[7]) == 6;

		/* Naive algorithm <1, O(n)> */
		LCANaive lcaNaive = new LCANaive(tree, 7);
		assert lcaNaive.RMQ(E[10], E[4]) == E[6];
		assert lcaNaive.RMQ(E[7], E[7]) == E[6];
		assert lcaNaive.RMQ(E[4], E[2]) == E[3];
		assert lcaNaive.RMQ(E[8], E[10]) == E[9];
		assert lcaNaive.RMQ(E[1], E[7]) == E[6];

		int i = 0;
		i++;
	}
}
