package Model;

/*An abstract class. Responsible for running a particular algorithm for a particular test and recording performance. */
public abstract class TestCase {

	private Stopwatch stopwatch = new Stopwatch();
	protected Tree tree;
	protected double elapsedTime = 0;
	protected boolean preprocessAlways;

	public TestCase(Tree tree, boolean preprocessAlways) {
		this.tree = tree;
		this.preprocessAlways = preprocessAlways;
	}

	protected void startStopwatch() {
		stopwatch.start();
	}

	protected void stopStopwatch() {
		this.elapsedTime += stopwatch.elapsedTime();
	}

	public double elapsedTime() {
		return this.elapsedTime;
	}

	public String getTestCaseName() {
		return String.format("%s N:%s; B:%s; D:%s; Single Runs: %s",
				getAlgorithmName(), tree.getN(), tree.getB(), tree.getD(),
				preprocessAlways);
	}

	public boolean getPreprocessAlways() {
		return this.preprocessAlways;
	}

	public abstract void preprocess();

	public abstract void executeQuery(INode node1, INode node2);

	protected abstract String getAlgorithmName();
}
