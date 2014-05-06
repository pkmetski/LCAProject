package Controller;

import java.io.IOException;
import java.util.Map;

import javax.naming.directory.InvalidAttributeValueException;

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

	private final int[][] multipliers;
	private final int[] baseProperties;
	private final Algorithm[] algorithms;
	private final int executions;
	private TreeController treeController = new TreeController();

	public TestController() {
		// N,B,D
		baseProperties = new int[] { 100000, 100, 3000 };

		// N multipliers,B multipliers,D multipliers
		multipliers = new int[][] { { 1, 2, 4, 8 }, { 1, 2, 4, 8 },
				{ 4, 8, 16, 32 } };

		executions = 1;
		algorithms = new Algorithm[] { Algorithm.Naive };
	}

	public void runTests() throws InvalidAttributeValueException, IOException,
			ClassNotFoundException {
		boolean preprocessAlways = false;

		// do {
		// first, loop through properties - N, B, D
		for (int currentProperty = 0; currentProperty < baseProperties.length; currentProperty++) {
			// next, loop through the multipliers for each property
			for (int currentMultiplier = 0; currentMultiplier < multipliers[currentProperty].length; currentMultiplier++) {

				// create the correct arguments for the current property and
				// multiplier
				int[] args = new int[baseProperties.length];
				for (int k = 0; k < baseProperties.length; k++) {
					if (k == currentProperty)
						args[k] = baseProperties[k]
								* multipliers[k][currentMultiplier];
					else
						args[k] = baseProperties[k] * multipliers[k][0];
				}
				Tree nonBaseTree = getTree(args);
				// test the non-base cases
				for (Algorithm algorithm : algorithms) {
					testAlgorithm(algorithm, nonBaseTree, preprocessAlways,
							executions);
				}
			}
		}
		preprocessAlways = !preprocessAlways;
		// } while (preprocessAlways);
	}

	private Tree getTree(int[] args) throws InvalidAttributeValueException,
			ClassNotFoundException, IOException {
		Tree tree = null;
		// first check if the tree already exists
		tree = TreeSerializer.deserialize(args[0], args[1], args[2]);
		// if it doesn't exist, create it
		if (tree == null) {
			tree = treeController.createTree(args);
			// save the tree
			TreeSerializer.serialize(tree);
		}
		return tree;
	}

	private void printResult(TestCase testCase, double result) {
		System.out.println(String.format("Test Case: %s; Running Time: %s",
				testCase.getTestCaseName(), result));
	}

	private void testAlgorithm(Algorithm algorithm, Tree tree,
			boolean preprocessAlways, int executions)
			throws InvalidAttributeValueException {
		TestCase testCase = null;

		switch (algorithm) {
		case Naive:
			testCase = new NaiveTestCase(tree, tree.getNode1(),
					tree.getNode2(), preprocessAlways);
			break;
		case SparseTable:
			testCase = new SparseTableTestCase(tree, tree.getNode1(),
					tree.getNode2(), preprocessAlways);
			break;
		case Restricted:
			testCase = new RestrictedTestCase(tree, tree.getNode1(),
					tree.getNode2(), preprocessAlways);
			break;
		default:
			throw new InvalidAttributeValueException(String.format(
					"Algorithm of type %s is not known", algorithm));
		}
		double result = runTestCase(testCase, executions);
		printResult(testCase, result);
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
