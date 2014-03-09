import Logic.EulerTourController;
import Logic.TreeController;
import Model.INode;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeController treeController = new TreeController();
		EulerTourController et = new EulerTourController(7);
		INode tree = treeController.getTree();

		et.tour(tree);
	}

}
