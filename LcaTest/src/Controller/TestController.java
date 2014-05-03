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

	private enum Algorithm {
		Naive, SparseTable, Restricted
	};

	private final int[] factors;
	private final int[] baseArgs;
	private final Algorithm[] algorithms;
	private final int executions;
	private TreeController treeController = new TreeController();

	public TestController() {
		factors = new int[] { 1, 2, 4, 8 };
		baseArgs = new int[] { 40000, 100, 2000 };
		executions = 1;
		algorithms = new Algorithm[] { Algorithm.Naive };
	}

	public void runTests() throws InvalidAttributeValueException {

		Map<TestCase, Double> results = new HashMap<TestCase, Double>();
		boolean preprocessAlways = false;

		// do {
		Tree baseTree = treeController.createTree(baseArgs);
		// test the base cases
		for (Algorithm algorithm : algorithms) {
			testAlgorithm(algorithm, baseTree, treeController.getNode1(),
					treeController.getNode2(), preprocessAlways, executions,
					results);
		}

		// first, loop through arguments - N, B, D
		for (int a = 0; a < baseArgs.length; a++) {
			// next, loop through factors - 2,4,8
			for (int f = 1; f < factors.length; f++) {

				// create the correct arguments for the current scale and
				// argument
				int[] nonBaseArgs = new int[baseArgs.length];
				for (int k = 0; k < baseArgs.length; k++) {
					if (k == a) {
						nonBaseArgs[k] = baseArgs[k] * factors[f];
					} else {
						nonBaseArgs[k] = baseArgs[k];
					}
				}
				Tree nonBaseTree = treeController.createTree(nonBaseArgs);
				// test the non-base cases
				for (Algorithm algorithm : algorithms) {
					testAlgorithm(algorithm, nonBaseTree, treeController.getNode1(),
							treeController.getNode2(), preprocessAlways,
							executions, results);
				}
			}
		}
		preprocessAlways = !preprocessAlways;
		// } while (preprocessAlways);

		printResults(results);
	}

	private void printResults(Map<TestCase, Double> results) {
		for (Map.Entry<TestCase, Double> entry : results.entrySet()) {
			System.out.println(String.format("Test Case: %s; Running Time: %s",
					entry.getKey().getTestCaseName(), entry.getValue()));
		}
	}

	private void testAlgorithm(Algorithm algorithm, Tree tree, INode node1,
			INode node2, boolean preprocessAlways, int executions,
			Map<TestCase, Double> results)
			throws InvalidAttributeValueException {
		TestCase testCase = null;

		switch (algorithm) {
		case Naive:
			testCase = new NaiveTestCase(tree, node1, node2, preprocessAlways);
			break;
		case SparseTable:
			testCase = new SparseTableTestCase(tree, node1, node2,
					preprocessAlways);
			break;
		case Restricted:
			testCase = new RestrictedTestCase(tree, node1, node2,
					preprocessAlways);
			break;
		default:
			throw new InvalidAttributeValueException(String.format(
					"Algorithm of type %s is not known", algorithm));
		}
		double result = runTestCase(testCase, executions);
		results.put(testCase, result);
	}

	private double runTestCase(TestCase testCase, int executions) {
		for (double i = 0; i < executions; i++) {
			if (i == 0 || testCase.getPreprocessAlways()) {
				testCase.preprocess();
			}
			testCase.executeQuery();
		}
		return testCase.elapsedTime() / executions;
	}
}
