import Logic.EulerTourController;
import Logic.RMQController;
import Logic.TreeController;
import Model.INode;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeController treeController = new TreeController();
		INode tree = treeController.getTree();

		EulerTourController et = new EulerTourController(7);
		et.process(tree);

		RMQController rmq = new RMQController();
		rmq.process(et.getR(), et.getL(), et.getE());
		
//		rmq.RMQ(u, v)
	}

}
