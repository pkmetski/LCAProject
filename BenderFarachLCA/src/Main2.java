import Logic2.EulerTourController;
import Logic2.RMQController;
import suffixtree.construction.SuffixTree;
import suffixtree.construction.Node;

public class Main2 {

	public static void main(String[] args) {
		String s = "ananas\1";
		Node root = SuffixTree.buildSuffixTree(s);

		EulerTourController et = new EulerTourController(7);
		et.process(root);

		RMQController rmq = new RMQController();
		rmq.process(et.getR(), et.getL(), et.getE());
		
//		rmq.RMQ(u, v)
	}

}
