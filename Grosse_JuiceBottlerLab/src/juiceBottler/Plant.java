package juiceBottler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Plant implements Runnable {

	public final int ORANGES_PER_BOTTLE = 3;

	public final int WORKERS_PER_PLANT = 4;

	private Worker[] workers;
	private Queue<Orange> oranges;
	private final Thread thread;
	private int orangesProvided;
	private int orangesProcessed;
	private volatile boolean timeToWork;

	public Plant(int threadNum) {
		orangesProvided = 0;
		orangesProcessed = 0;
		thread = new Thread(this, "Plant[" + threadNum + "]");
		oranges = new LinkedBlockingQueue<Orange>();
		workers = new Worker[WORKERS_PER_PLANT];
		for (int i = 1; i <= WORKERS_PER_PLANT; i++) {
			workers[i - 1] = new Worker(i, this);
		}
	}

	public void startPlant() {
		timeToWork = true;
		for (Worker w : workers) {
			w.startWorker();
		}
		thread.start();
	}

	public void stopPlant() {
		for (Worker w : workers) {
			w.stopWorker();
		}
		timeToWork = false;
	}

	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " plant stop malfunction");
		}
	}
	
	@Override
	public void run() {
		System.out.println(this.getThreadName() + " processing oranges");
		
		while (timeToWork) {
			continue;
		}
		
		for (Worker w : workers) {
			w.waitToStop();
		}
		
		System.out.println(this.getThreadName() + " is Finished");
	}

	public synchronized void fetchOrange() {
		oranges.add(new Orange());
		orangeProvided();
	}
	
	public synchronized Orange requestOrange() {
		if (oranges.isEmpty()) {
			fetchOrange();
		}
		return oranges.remove();
	}

	public synchronized void returnOrange(Orange o) {
		if (o != null) {
			if (o.getState() == Orange.State.Processed) {
				orangeProcessed();
			} else {
				oranges.add(o);
			}
		}
	}

	public synchronized void orangeProvided() {
		orangesProvided++;
	}

	public synchronized void orangeProcessed() {
		orangesProcessed++;
	}

	public int getProvidedOranges() {
		return orangesProvided;
	}

	public int getProcessedOranges() {
		return orangesProcessed;
	}

	public int getBottles() {
		return orangesProcessed / ORANGES_PER_BOTTLE;
	}

	public int getWaste() {
		return orangesProcessed % ORANGES_PER_BOTTLE;
	}

	public String getThreadName() {
		return this.thread.getName();
	}
}