package juiceBottler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @desc The unethical bodies themselves. A juicing plant which utilizes slave
 *       labor to juice oranges
 * @author Jake with reference to Nate Williams's code
 *
 */
public class Plant implements Runnable {

	// User Determined Variables
	public final int ORANGES_PER_BOTTLE = 3;
	public final int WORKERS_PER_PLANT = 4;

	// Instance Variables
	private Worker[] workers;
	// Queue of oranges for FIFO
	private Queue<Orange> oranges;
	private final Thread thread;
	private int orangesProvided;
	private int orangesProcessed;
	// volatile to ensure freshness (possibly redundant, doesn't hurt)
	// used to inform this plant that it can loop to work
	private volatile boolean timeToWork;

	public Plant(int threadNum) {
		// init incrementors to 0
		orangesProvided = 0;
		orangesProcessed = 0;
		// thread running "this" and named by the String
		thread = new Thread(this, "Plant[" + threadNum + "]");

		// LinkedBlockingQueue for some added efficiency over a list or Array based
		// implementation
		oranges = new LinkedBlockingQueue<Orange>();
		
		// init workers in array
		workers = new Worker[WORKERS_PER_PLANT];
		for (int i = 1; i <= WORKERS_PER_PLANT; i++) {
			workers[i - 1] = new Worker(i, this);
		}
	}

	/**
	 * @desc Starts the plant and starts each worker in the plant
	 */
	public void startPlant() {
		timeToWork = true;
		for (Worker w : workers) {
			w.startWorker();
		}
		thread.start();
	}

	/**
	 * @desc Informs the plant it is time to stop and does the same for the workers
	 */
	public void stopPlant() {
		for (Worker w : workers) {
			w.stopWorker();
		}
		timeToWork = false;
	}

	/**
	 * @desc Joins threads and kills them, errors if interrupted
	 */
	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " plant stop malfunction");
		}
	}

	/**
	 * @desc is the method the plant runs while in use. prints status of plant and,
	 *       after working, joins and kills worker threads. This occurs only after
	 *       the plant has already been stopped
	 */
	@Override
	public void run() {
		// Console update
		System.out.println(this.getThreadName() + " processing oranges");

		// Allow workers to work
		while (timeToWork) {
			continue;
		}

		// Kill worker threads
		for (Worker w : workers) {
			w.waitToStop();
		}

		// Console update
		System.out.println(this.getThreadName() + " is Finished");
	}

	/**
	 * @desc Creates new orange and adds it to the queue. Increments the number of
	 *       oranges provided
	 */
	public synchronized void fetchOrange() {
		oranges.add(new Orange());
		orangeProvided();
	}

	/**
	 * @desc gets an orange if the queue of oranges is empty, returns an orange
	 * @return Orange
	 */
	public synchronized Orange requestOrange() {
		if (oranges.isEmpty()) {
			fetchOrange();
		}
		return oranges.remove();
	}

	/**
	 * @desc a method of giving an orange back to the plant which gave the worker
	 *       the orange in the first place
	 * @param o the Orange to return
	 */
	public synchronized void returnOrange(Orange o) {
		if (o != null) {
			if (o.getState() == Orange.State.Processed) {
				orangeProcessed();
			} else {
				oranges.add(o);
			}
		}
	}

	/**
	 * @desc increments orangesProvided
	 */
	public synchronized void orangeProvided() {
		orangesProvided++;
	}

	/**
	 * @desc increments orangesProcessed
	 */
	public synchronized void orangeProcessed() {
		orangesProcessed++;
	}

	/**
	 * @desc gets the number of oranges provided
	 * @return int
	 */
	public int getProvidedOranges() {
		return orangesProvided;
	}

	/**
	 * @desc gets the number of oranges processed
	 * @return int
	 */
	public int getProcessedOranges() {
		return orangesProcessed;
	}

	/**
	 * @desc gets the number of OJ bottles produced
	 * @return int
	 */
	public int getBottles() {
		return orangesProcessed / ORANGES_PER_BOTTLE;
	}

	/**
	 * @desc gets the number of wasted oranges
	 * @return int
	 */
	public int getWaste() {
		return orangesProvided - orangesProcessed;
	}

	/**
	 * @desc gets the name of the thread running the instance of the plant which
	 *       calls this method
	 * @return String
	 */
	public String getThreadName() {
		return this.thread.getName();
	}
}