package juiceBottler;

/**
 * @desc The manager of highly unethical plants which rely on slave labor to
 *       produce orange juice. Initializes plants, stops plants, and sums the
 *       results of multiple plants.
 * @author Jake with reference to Nate Williams's code
 *
 */
public class PlantManager {

	// Time given to the plants to process oranges
	public static final long PROCESSING_TIME = 5000;

	// Number of total plants running at a time
	private static final int NUM_PLANTS = 2;

	public static void main(String[] args) {
		// Startup the plants (create and start thread)
		Plant[] plants = new Plant[NUM_PLANTS];
		// fill array and start
		for (int i = 0; i < NUM_PLANTS; i++) {
			plants[i] = new Plant(i);
			plants[i].startPlant();
		}

		// Give the plants time to do work, error if interrupted
		delay(PROCESSING_TIME, "Plant malfunction");

		// Tell plants to stop working
		for (Plant p : plants) {
			p.stopPlant();
		}
		// Wait for plants to be done before killing the threads
		for (Plant p : plants) {
			p.waitToStop();
		}

		// Summarize the results
		int totalProvided = 0;
		int totalProcessed = 0;
		int totalBottles = 0;
		int totalWasted = 0;
		// add totals for each plant together
		for (Plant p : plants) {
			totalProvided += p.getProvidedOranges();
			totalProcessed += p.getProcessedOranges();
			totalBottles += p.getBottles();
			totalWasted += p.getWaste();
		}

		// print results
		System.out.println("Total provided/processed = " + totalProvided + "/" + totalProcessed);
		System.out.println("Created " + totalBottles + ", wasted " + totalWasted + " oranges");
	}

	/**
	 * @desc Sleeps the plant manager thread to allow plants to run, prints error if
	 *       the thread can't have his nap
	 * @param time   time to delay
	 * @param errMsg error which should be printed if the method fails
	 */
	private static void delay(long time, String errMsg) {
		// max used to ensure a non-negative time (which would run forever)
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}
}