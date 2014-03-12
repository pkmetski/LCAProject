import Logic.EulerTourController;
import Logic.RMQController;
import Logic.TreeController;
import Model.INode;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeController treeController = new TreeController(7);
		INode tree = treeController.getTree();

		EulerTourController et = new EulerTourController(7);
		et.process(tree);

		RMQController rmq = new RMQController();
		rmq.process(et.getR(), et.getE(), et.getL());

		int rmqIndex = rmq.RMQ(4, 10);
		int rmqIndex2 = rmq.RMQ(4, 7);
		int rmqIndex3 = rmq.RMQ(2, 4);
		int rmqIndex4 = rmq.RMQ(8, 10);
		int rmqIndex5 = rmq.RMQ(1, 7);

		int i = 0;
		i++;
	}
}
