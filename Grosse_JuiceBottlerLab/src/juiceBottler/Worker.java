package juiceBottler;

/**
 * @desc The slave worker of the bottling plants. He got the short end of the
 *       stick. If you see another set of commented code within this file in git
 *       history, it was Matt Bushnell's code which I got and looked at AFTER I
 *       completed my code; I did not copy his code, I was using his fully
 *       functional code to test what part of my program didn't work. It turned
 *       out the only error I made was not telling the thread constructor in
 *       worker that it should run "this" (the object which needs to run on the
 *       thread) before giving it a string for the name.
 * @author Jake Grosse
 *
 */
public class Worker implements Runnable {

	// Instance Variables

	// a worker must know what plant they are employed by
	private final Plant p;
	// volatile to ensure constantly updated (possibly redundant, doesn't hurt)
	private volatile boolean timeToWork;
	private final Thread thread;
	// All of our workers are slaves to the company, endentured for transit to
	// Orangetopia
	@SuppressWarnings("unused")
	private static final String WORKER_SALARY = "slave labor";

	public Worker(int workerNum, Plant p) {
		// initialize instance variables
		this.thread = new Thread(this, "Slave [" + workerNum + "]");
		this.p = p;
		timeToWork = false;
	}

	/**
	 * @desc Starts worker thread
	 */
	public void startWorker() {
		timeToWork = true;
		thread.start();
	}

	/**
	 * @desc executes on the worker's individual thread to process one step of an
	 *       orange
	 */
	public void run() {
		while (timeToWork) {
			// get an orange
			Orange o = p.requestOrange();
			// print the status of the orange
			System.out.println(p.getThreadName() + " " + this.thread.getName() + o.getState() + " orange");
			// does one process on the orange
			o.runProcess();
			// gives the orange back to the plant
			p.returnOrange(o);
		}
		// prints when worker is done for user verification
		System.out.println(thread.getName() + " is Finished");
	}

	/**
	 * @desc Ends run loop of worker
	 */
	public void stopWorker() {
		timeToWork = false;
	}

	/**
	 * @desc stops worker, throws error if Interrupted when stopping
	 */
	public void waitToStop() {
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " slave stop malfunction");
		}
	}
}