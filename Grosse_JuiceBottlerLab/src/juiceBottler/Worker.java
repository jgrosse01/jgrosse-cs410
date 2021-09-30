package juiceBottler;

public class Worker implements Runnable {

	private Plant p;
	private volatile boolean timeToWork;
	private volatile boolean finishedAssignedTask;
	private final Thread thread;

	public Worker(int workerNum, Plant p) {
		this.p = p;
		this.thread = new Thread("Worker [" + workerNum + "]");
	}

	public void startWorker() {
		timeToWork = true;
		thread.start();
	}

	public void stopWorker() {
		timeToWork = false;
	}

	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " worker stop malfunction");
		}
	}

	@Override
	public void run() {
		while (timeToWork) {
			finishedAssignedTask = false;
			Orange o = p.requestOrange();
			if (!finishedAssignedTask) {
				o.runProcess();
				System.out.println(p.getThreadName() + " " + this.thread.getName() + o.getState() + " orange");

				finishedAssignedTask = true;
			}
			p.returnOrange(o);
		}
	}
}
