
public class Person implements Runnable{
	private static int threadCount = 0;
	private BombShelter bs;
	
	public Person(BombShelter bs) {
		this.bs = bs;
		new Thread(this, "Person[" + ++threadCount + "]").start();
	}

	@Override
	public void run() {
		if (!bs.savePerson(this)) {
			System.err.println("I died!");
		}
	}
}
