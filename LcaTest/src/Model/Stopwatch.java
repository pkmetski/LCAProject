package Model;

public class Stopwatch {

	private long start;

	public void start() {
		start = System.currentTimeMillis();
	}

	// return time (in milliseconds) since this object was created
	public double elapsedTime() {
		return System.currentTimeMillis() - start;
	}
}
