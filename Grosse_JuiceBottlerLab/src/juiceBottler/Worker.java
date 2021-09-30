package juiceBottler;

//public class Worker implements Runnable{
//    //Class variables
//    private volatile boolean working; //Flag for running thread
//    public final Thread thread; //The thread that runs
//    public final Plant plant; //Plant that the worker works for
//
//    //Constructor for worker, uses same naming convention as Plant + the plant the worker is from
//    Worker(int threadNum, Plant p) {
//        thread = new Thread(this, "Worker ["+threadNum+"]" + " " + p.getThreadName());
//        this.plant = p;
//        working = false;
//    }
//
//    //Signal to the worker to start working
//    public void startWorker() {
//        working = true;
//        thread.start();
//    }
//
//    /**
//     * @desc While the worker is working they will get an orange from the plant, run
//     * the next process for the orange, and either return it to the plant or discard it and
//     * run incProcessedOranges if the orange is done being processed.
//     */
//    public void run() {
//        while (working) {
//            Orange o = plant.requestOrange();
//            System.out.println(thread.getName() + " " + o.getState() + " orange");
//            o.runProcess();
//            if(o.getState() == Orange.State.Processed) {
//                plant.orangeProcessed();
//            } else {
//                plant.returnOrange(o);
//            }
//        }
//        System.out.println(thread.getName() + " Done");
//    }
//
//    public void stopWorker() {
//        working = false;
//    }
//    
//    public void waitToStop() {
//    	try {
//    		thread.join();
//    	} catch (InterruptedException e) {
//    		System.err.println(thread.getName() + " worker stop malfunction");
//    	}
//    }
//
//}



public class Worker implements Runnable {

	private final Plant p;
	private volatile boolean timeToWork;
	private final Thread thread;

	public Worker(int workerNum, Plant p) {
		this.thread = new Thread(this, "Worker [" + workerNum + "]");
		this.p = p;
		timeToWork = false;
	}

	public void startWorker() {
		timeToWork = true;
		thread.start();
	}
	
	public void run() {
		while (timeToWork) {
			Orange o = p.requestOrange();
			System.out.println(p.getThreadName() + " " + this.thread.getName() + o.getState() + " orange");
			o.runProcess();
			p.returnOrange(o);
		}
		System.out.println(thread.getName() + " is Finished");
	}

	public void stopWorker() {
		timeToWork = false;
	}

	public void waitToStop() {
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			System.err.println(thread.getName() + " worker stop malfunction");
		}
	}
}