import java.util.ArrayList;

public class BombShelter {
	public static void main(String[] args) {
		BombShelter bs = new BombShelter();
		Person[] persons = new Person[CAPACITY];
		for (int i = 0; i < CAPACITY; i++) {
			persons[i] = new Person(bs);
			if (!bs.savePerson(persons[i])) {
				System.err.println("No Room In Shelter");
			}
		}
		
		Person extraPerson = new Person(bs);
		if (!bs.savePerson(extraPerson)) {
			System.err.println("Expected Failure: No Room In Shelter");
		}
	}
	
	private static final int CAPACITY = 7;
	private ArrayList<Person> saved = new ArrayList<Person>();
	
	BombShelter() {
		
	}
	
	public boolean savePerson(Person p) {
		if (saved.size() < CAPACITY) {
			saved.add(p);
			return true;
		}
		else {
			return false;
		}
	}
	
	public void leaveShelter(Person p) {
		saved.remove(p);
	}
	
	public void allClear() {
		for (Person p : saved) {
			saved.remove(p);
		}
	}
}
