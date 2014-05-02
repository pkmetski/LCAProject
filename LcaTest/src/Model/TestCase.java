package Model;

/*An abstract class. Responsible for running a particular algorithm for a particular test and recording performance. */
public abstract class TestCase {

	private Stopwatch stopwatch = new Stopwatch();
	protected Tree tree;
	protected INode node1, node2;
	protected double elapsedTime = 0;
	protected boolean preprocessAlways;

	public TestCase(Tree tree, INode node1, INode node2,
			boolean preprocessAlways) {
		this.tree = tree;
		this.node1 = node1;
		this.node2 = node2;
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
		return String.format("%s N: %s; B: %s; D: %s; Time: %s",
				getAlgorithmName(), tree.getN(), tree.getB(), tree.getD(),
				elapsedTime);
	}

	public boolean getPreprocessAlways() {
		return this.preprocessAlways;
	}

	public abstract void preprocess();

	public abstract void executeQuery();

	protected abstract String getAlgorithmName();
}
