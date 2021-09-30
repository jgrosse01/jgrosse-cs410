package juiceBottler;

/**
 * @desc a class used to manage the plants spread across the nation of Juice
 * @author Jake Grosse with heavy influence from Nate Williams
 *
 */
public class PlantManger {

	// user determined constants
	public static final int NUM_PLANTS = 2;
	public static final long PROCESS_TIME = 5000;

	// run the program!!! Duh
	public static void main(String[] args) {

		// create plants
		Plant[] plants = new Plant[NUM_PLANTS];
		for (int i = 0; i < NUM_PLANTS; i++) {
			plants[i] = new Plant(i);
		}

		// starts plants
		for (Plant p : plants) {
			p.startPlant();
		}

		// allows time for plants to process. if the thread cannot sleep, throws error
		// message passed by parameter
		delay(PROCESS_TIME, "Plant Malfunction");

		// tells plants not to start any more work
		for (Plant p : plants) {
			p.stopPlant();
		}

		// stops plant threads
		for (Plant p : plants) {
			p.waitToStop();
		}

		// Summarizing of the important values of a juice bottling plant
		int totalFetched = 0;
		int totalProcessed = 0;
		int totalBottles = 0;
		int totalWasted = 0;

		// makes sure to include each plant in the total calculation
		for (Plant p : plants) {
			totalFetched += p.getFetched();
			totalProcessed += p.getProcessed();
			totalBottles += p.getBottled();
			totalWasted += p.getWasted();
		}

		// printed summary of results from the plants
		System.out.println("Total provided/processed = " + totalFetched + "/" + totalProcessed);
		System.out.println("Created " + totalBottles + ", wasted " + totalWasted + " oranges");
	}

	/**
	 * @desc method which puts threads to sleep for a specific amount of time and
	 *       throws an error if that fails
	 * @param time   that the thread is to sleep for
	 * @param errMsg that will be displayed should the thread not sleeps
	 */
	private static void delay(long time, String errMsg) {
		// max function used to ensure time is not negative (infinite)
		long sleepTime = Math.max(1, time);
		// sleep thread or display error
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}
}
