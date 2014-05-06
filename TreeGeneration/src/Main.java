import javax.naming.directory.InvalidAttributeValueException;

import Controller.TreeController;
import Model.Tree;

public class Main {

	public static void main(String[] args) {

		TreeController tc = new TreeController();
		try {
			Tree tree = tc.createTree(new int[] { 1000000, 200, 100000 });
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
