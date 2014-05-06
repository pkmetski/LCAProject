import javax.naming.directory.InvalidAttributeValueException;

import Controller.TestController;

public class Main {

	public static void main(String[] args) {
		TestController tst = new TestController();
		try {
			tst.runTests();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
