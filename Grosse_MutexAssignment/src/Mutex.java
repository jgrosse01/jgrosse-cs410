
public class Mutex {
	
	private static boolean inUse = false;

	public Mutex() {}
	
	public synchronized boolean acquire() {
		if (inUse) {
			return false;
		} else {
			inUse = true;
			return inUse;
		} 
	}
	
	public synchronized void release() {
		inUse = false;
	}
}
