package juiceBottler;

import juiceBottler.Orange;

/**
 * @desc A worker in the bottling plant. Processors of Oranges. Implements
 *       runnable
 * @author Jake Grosse
 *
 */
public class Worker implements Runnable {

	// instance variables
	private Orange o;
	private Thread thread;

	// boolean flag to check if the worker has an orange
	private boolean hasOrange;

	// boolean flag to check if the worker should be working
	private boolean timeToWork;

	// Worker constructor
	public Worker(int numberOfWorkers) {
		// initialize variables
		this.o = null;
		hasOrange = false;
		timeToWork = false;
		// initialize thread and name it
		this.thread = new Thread("Worker[" + numberOfWorkers + "]");
	}

	/**
	 * @desc function called when the thread executes its task. Processes Oranges by
	 *       one step
	 */
	@Override
	public void run() {
		if (timeToWork) {
			if (hasOrange()) {
				// processes the orange one step
				o.runProcess();
				// print the plant number, the worker number, and the task in relation to the
				// orange.
				System.out.println(
						Thread.currentThread().getName() + " " + thread.getName() + " " + o.getState() + " Orange");
			}
		}
	}

	/**
	 * @desc starts an individual worker thread
	 */
	public void startWorker() {
		timeToWork = true;
		thread.start();
	}

	/**
	 * @desc tells an individual worker it is time to stop working without
	 *       terminating the thread
	 */
	public void stopWorker() {
		timeToWork = false;
	}

	/**
	 * @desc terminates the thread, if this fails, throws an InterruptedException
	 */
	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " stop malfunction");
		}
	}

	/**
	 * @desc method of giving an orange to an individual worker
	 * @param o is an orange to be passed in
	 */
	public synchronized void giveOrange(Orange o) {
		this.o = o;
		hasOrange = true;
	}

	/**
	 * @desc synchronized method of returning an orange from the worker to the plant
	 *       to be stored for further processing or total-incrementing
	 * @return Orange
	 */
	public synchronized Orange retOrange() {
		// returns the orange then sets the orange to null
		try {
			hasOrange = false;
			return o;
		} finally {
			this.o = null;
		}
	}

	/**
	 * @desc synchronized method of checking whether the worker has an orange
	 * @return
	 */
	public synchronized boolean hasOrange() {
		return hasOrange;
	}
}
