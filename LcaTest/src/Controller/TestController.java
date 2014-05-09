package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

	private final int[][] multipliers;
	private final int[] baseProperties;
	private final Algorithm[] algorithms;
	private final int executions;
	private TreeController treeController = new TreeController();

	public TestController() {
		// N,B,D
		baseProperties = new int[] { 700000, 100, 80000 };

		// N multipliers,B multipliers,D multipliers
		multipliers = new int[][] { { 1, 2, 4, 8 }, { 1, 2, 4, 8 },
				{ 1, 2, 4, 8 } };

		executions = 10;
		algorithms = new Algorithm[] { Algorithm.Naive, Algorithm.Restricted };
	}

	public void runTests() throws InvalidAttributeValueException, IOException,
			ClassNotFoundException {
		boolean preprocessAlways = false;

		do {
			// first, loop through properties - N, B, D
			for (int currentProperty = baseProperties.length - 1; 0 <= currentProperty; currentProperty--) {
				// next, loop through the multipliers for each property
				for (int currentMultiplier = 3; currentMultiplier < multipliers[currentProperty].length; currentMultiplier++) {

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
					Tree tree = getTree(args);
					// test the non-base cases
					for (Algorithm algorithm : algorithms) {
						testAlgorithm(algorithm, tree, preprocessAlways,
								executions);
					}
				}
			}
			preprocessAlways = !preprocessAlways;
		} while (preprocessAlways);
	}

	private Tree getTree(int[] args) throws InvalidAttributeValueException,
			ClassNotFoundException, IOException {
		// first check if the tree already exists
		Tree tree = null;
		// tree = TreeSerializer.deserialize(args[0], args[1], args[2]);
		// if it doesn't exist, create it
		if (tree == null) {
			tree = treeController.createTree(args);
			// save the tree
			// TreeSerializer.serialize(tree);
		}
		return tree;
	}

	private void printResult(TestCase testCase, double result)
			throws IOException {
		System.out.println(String.format("Test Case: %s; Running Time: %s",
				testCase.getTestCaseName(), result));

		// saveTest(testCase, result, currentTest);
	}

	private void testAlgorithm(Algorithm algorithm, Tree tree,
			boolean preprocessAlways, int executions)
			throws InvalidAttributeValueException, IOException {
		TestCase testCase = null;

		switch (algorithm) {
		case Naive:
			testCase = new NaiveTestCase(tree, preprocessAlways);
			break;
		case SparseTable:
			testCase = new SparseTableTestCase(tree, preprocessAlways);
			break;
		case Restricted:
			testCase = new RestrictedTestCase(tree, preprocessAlways);
			break;
		default:
			throw new InvalidAttributeValueException(String.format(
					"Algorithm of type %s is not known", algorithm));
		}
		double result = runTestCase(testCase, tree, executions);
		printResult(testCase, result);
	}

	private double runTestCase(TestCase testCase, Tree tree, int executions) {
		for (double i = 0; i < executions; i++) {
			if (i == 0 || testCase.getPreprocessAlways()) {
				testCase.preprocess();
			}
			testCase.executeQuery(
					treeController.getRandomNode(tree, tree.getN()),
					treeController.getRandomNode(tree, tree.getN()));
		}
		return testCase.elapsedTime() / executions;
	}

	private void saveTest(TestCase testCase, double result, int currentTest)
			throws IOException {
		String path = "C:\\Results\\";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		int filename = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				int name = Integer.parseInt(listOfFiles[i].getName());
				if (name > filename) {
					filename = name;
				}
			}
		}
		// get this file's last line
		// if it is 1 less than the current line, use this file
		// otherwise, create new
		File f = new File(path + filename);
		if (!f.exists()) {
			f.createNewFile();
		}
		BufferedReader br = new BufferedReader(new FileReader(path + filename));
		String line;
		while ((line = br.readLine()) != null) {
			int lastTest = Integer
					.parseInt(line.substring(0, line.indexOf(".")));
			if (lastTest + 1 != currentTest) {
				filename++;
			}
		}
		br.close();

		FileWriter fw = new FileWriter(path + filename);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		bw.write(String.format("%s. Test Case: %s; Running Time: %s",
				currentTest, testCase.getTestCaseName(), result));
		bw.close();
	}
}
