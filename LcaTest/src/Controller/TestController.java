package Controller;

import java.util.HashMap;
import java.util.Map;
import javax.naming.directory.InvalidAttributeValueException;
import Model.INode;
import Model.NaiveTestCase;
import Model.RestrictedTestCase;
import Model.SparseTableTestCase;
import Model.TestCase;
import Model.Tree;

/*
 * Responsible for generating trees, assigning node1 and node2, advancing to
 * the next test case and recording test data
 */
public class TestController {

	private final int[] factors;
	private final int[] arguments;
	private TreeController treeController = new TreeController();

	public TestController() {
		factors = new int[] { 1, 2, 4, 8 };
		arguments = new int[] { 2400, 100, 200 };
	}

	public void runTests() throws InvalidAttributeValueException {

		Map<TestCase, Double> results = new HashMap<TestCase, Double>();
		int executions = 10;
		boolean preprocessAlways = false;

		do {

			// naive alg base case
			Tree naiveBaseTree = treeController.createTree(arguments);
			INode node1 = treeController.getNode1(), node2 = treeController
					.getNode2();
			TestCase naiveBaseTestCase = new NaiveTestCase(naiveBaseTree,
					node1, node2, preprocessAlways);
			recordResult(results, naiveBaseTestCase, executions);

			// sparse table alg base case
			Tree sparseBaseTree = treeController.createTree(arguments);
			TestCase sparseBaseTestCase = new SparseTableTestCase(
					sparseBaseTree, node1, node2, preprocessAlways);
			recordResult(results, sparseBaseTestCase, executions);

			// restricted alg base case
			Tree restrictedBaseTree = treeController.createTree(arguments);
			TestCase restrictedBaseTestCase = new RestrictedTestCase(
					restrictedBaseTree, node1, node2, preprocessAlways);
			recordResult(results, restrictedBaseTestCase, executions);

			// first, loop through arguments - N, B, D
			for (int a = 0; a < arguments.length; a++) {
				// next, loop through factors - 2,4,8
				for (int f = 1; f < factors.length; f++) {

					// create the correct arguments for the current scale and
					// argument
					int[] args = new int[arguments.length];
					for (int k = 0; k < arguments.length; k++) {
						if (k == a) {
							args[k] = arguments[k] * factors[f];
						} else {
							args[k] = arguments[k];
						}
					}

					// for each algorithm, create a tree with the specific
					// arguments

					// naive alg
					Tree naiveAlgTree = treeController.createTree(args);
					node1 = treeController.getNode1();
					node2 = treeController.getNode2();
					TestCase naiveTestCase = new NaiveTestCase(naiveAlgTree,
							node1, node2, preprocessAlways);
					recordResult(results, naiveTestCase, executions);

					// sparse table alg
					Tree sparseAlgTree = treeController.createTree(args);
					TestCase sparseTestCase = new SparseTableTestCase(
							sparseAlgTree, node1, node2, preprocessAlways);
					recordResult(results, sparseTestCase, executions);

					// restricted alg
					Tree restrictedAlgTree = treeController.createTree(args);
					TestCase restrictedTestCase = new RestrictedTestCase(
							restrictedAlgTree, node1, node2, preprocessAlways);
					recordResult(results, restrictedTestCase, executions);
				}
			}
			preprocessAlways = !preprocessAlways;
		} while (preprocessAlways);

		printResults(results);
	}

	private double runTestCase(TestCase testCase, int executions) {

		return 0;
		// for (int i = 0; i < executions; i++) {
		// if (i == 0 || testCase.getPreprocessAlways()) {
		// testCase.preprocess();
		// }
		// testCase.executeQuery();
		// }
		// return testCase.elapsedTime() / executions;
	}

	private void recordResult(Map<TestCase, Double> results, TestCase testCase,
			int executions) {
		results.put(testCase, runTestCase(testCase, executions));
	}

	private void printResults(Map<TestCase, Double> results) {
		for (Map.Entry<TestCase, Double> entry : results.entrySet()) {
			System.out.println(String.format("Test Case: %s; Running Time: %s",
					entry.getKey().getTestCaseName(), entry.getValue()));
		}
	}
}
