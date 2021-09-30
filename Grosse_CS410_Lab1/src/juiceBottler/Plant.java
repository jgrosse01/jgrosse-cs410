package juiceBottler;

import java.util.List;
import java.util.LinkedList;

import juiceBottler.Orange;
import juiceBottler.Plant;

/**
 * @desc the bottling plant which employs workers to juice and bottle oranges.
 *       implements runnable
 * @author Jake Grosse with influence from Nate Williams
 * 
 */
public class Plant implements Runnable {

	// Controlled Rates
	private final int ORANGES_PER_BOTTLE = 4;
	private final int NUM_WORKERS = 4;

	// Instance Variables
	private final Thread thread;
	private int orangesProvided;
	private int orangesProcessed;

	// boolean check to ensure the plant is meant to be running
	private boolean timeToWork;

	// array of workers to assign tasks and a list of oranges to store extras
	private Worker[] workers;
	private List<Orange> oranges;

	public Plant(int threadNum) {
		orangesProvided = 0;
		orangesProcessed = 0;

		workers = new Worker[NUM_WORKERS];
		oranges = new LinkedList<Orange>();

		// fill array of workers with workers
		for (int i = 0; i < NUM_WORKERS; i++) {
			workers[i] = new Worker(i);
		}

		// initialize thread and name it
		thread = new Thread(this, "Plant[" + threadNum + "]");
	}

	/**
	 * @desc starts the juice bottling plant and its workers
	 */
	public void startPlant() {
		timeToWork = true;
		for (Worker w : workers) {
			w.startWorker();
		}
		thread.start();
	}

	/**
	 * @desc tells workers and plant that it is time to stop while not terminating
	 *       tasks
	 */
	public void stopPlant() {
		for (Worker w : workers) {
			w.stopWorker();
		}

		timeToWork = false;
	}

	/**
	 * @desc after tasks are complete, attempts to join the worker threads and the
	 *       plant threads, throws InterruptedException if it cannot complete this
	 *       task
	 */
	public void waitToStop() {
		for (Worker w : workers) {
			w.waitToStop();
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
	}

	/**
	 * @desc function which executes on thread call. assigns worker an orange, runs
	 *       the worker, and gets the orange back from the worker
	 */
	public void run() {
		// notes the initial start of the plant itself
		System.out.println(Thread.currentThread().getName() + " Processing oranges");
		// while the plant is on
		while (timeToWork) {
			// each worker should be:
			for (Worker w : workers) {
				// assigned an orange
				w.giveOrange(getOrange());
				// told to process one step of the orange
				w.run();
				// told to give back the orange
				Orange temp = w.retOrange();
				// null check just in case there is a worker that doesn't have an orange
				if (temp != null) {
					// if it is finished then add to the total
					if (temp.getState() == Orange.State.Bottled) {
						orangeProcessed();
						// if it is not finished then add it back into the list of oranges to be
						// processed
					} else {
						oranges.add(temp);
					}
				}
			}
		}

		// notify upon thread finish
		System.out.println(Thread.currentThread().getName() + " is Finished");
	}

	/**
	 * @desc synchronized method to get an orange for a worker
	 * @return Orange
	 */
	public synchronized Orange getOrange() {
		// the idea for this was acquired from Matt Bushnell, the code however, is my
		// own work
		// if there is an orange in the list take one from the end and return it
		if (!oranges.isEmpty()) {
			int end = oranges.size();
			return oranges.remove(end - 1);
		} else {
			// if there is no orange in the list, increment the provided oranges and make a
			// new orange
			orangeFetched();
			return new Orange();
		}
	}

	/**
	 * @desc synchronized helper to increment the count of provided oranges in a
	 *       thread-safe manner
	 */
	public synchronized void orangeFetched() {
		orangesProvided++;
	}

	/**
	 * @desc synchronized helper to increment the count of processed oranges in a
	 *       thread-safe manner
	 */
	public synchronized void orangeProcessed() {
		orangesProcessed++;
	}

	/**
	 * @desc gets the number of fetched oranges
	 * @return int
	 */
	public int getFetched() {
		return orangesProvided;
	}

	/**
	 * @desc gets the number of processed oranges
	 * @return int
	 */
	public int getProcessed() {
		return orangesProcessed;
	}

	/**
	 * @desc gets the number of oranges in total that were bottled (processed /
	 *       oranges per bottle)
	 * @return int
	 */
	public int getBottled() {
		return orangesProcessed / ORANGES_PER_BOTTLE;
	}

	/**
	 * @desc returns the number of wasted oranges (remainder of the number of
	 *       bottles)
	 * @return int
	 */
	public int getWasted() {
		return orangesProcessed % ORANGES_PER_BOTTLE;
	}

}