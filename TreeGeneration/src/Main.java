import javax.naming.directory.InvalidAttributeValueException;

import Controller.TreeController;
import Model.Tree;

public class Main {

	public static void main(String[] args) {

		TreeController tc = new TreeController();
		try {
			Tree tree = tc.createTree(new int[] { 5000, 2000, 10 });
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
