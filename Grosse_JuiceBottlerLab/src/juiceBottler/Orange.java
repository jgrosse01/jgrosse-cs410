package juiceBottler;

/**
 * @desc Class with embedded enumerator to implement a state system for oranges.
 *       The orange class itself handles the state the orange is in and the
 *       changing of said state.
 * @author Nate Williams copied by Jake Grosse
 *
 */
public class Orange {

	/**
	 * @desc Embedded enum to handle state of an orange and how long each process
	 *       takes in ms
	 * @author Nate Williams copied by Jake Grosse
	 *
	 */
	public enum State {
		Fetched(15), Peeled(38), Squeezed(29), Bottled(17), Processed(1);

		// set final state to "Processed"
		private static final int finalIndex = State.values().length - 1;

		final int timeToComplete;

		// constructor, only commenting this because I've never worked with enums before
		// and this helps me understand it :)
		State(int timeToComplete) {
			this.timeToComplete = timeToComplete;
		}

		/**
		 * @desc A method used to return the next state in the sequence
		 * @return State
		 */
		State getNext() {
			// gets current order from the programmer defined order above
			int currIndex = this.ordinal();
			// if it's index in relation is past the maximum state, error
			if (currIndex >= finalIndex) {
				throw new IllegalStateException("Already at final state");
			}
			// returns the next state
			return State.values()[currIndex + 1];
		}
	}

	// now were back at Orange class

	// instance variables
	private State state;

	public Orange() {
		state = State.Fetched;
		doWork();
	}

	/**
	 * @desc returns the state of the orange
	 * @return State
	 */
	public State getState() {
		return state;
	}

	/**
	 * @desc sleeps for the predefined amount, then increments orange state
	 */
	public void runProcess() {
		// Don't attempt to process an already completed orange
		if (state == State.Processed) {
			throw new IllegalStateException("This orange has already been processed");
		}
		doWork();
		state = state.getNext();
	}

	// helper method for runProcess() which sleeps the thread working on the orange
	// for the processing time defined in the enum above
	private void doWork() {
		// Sleep for the amount of time necessary to do the work
		try {
			Thread.sleep(state.timeToComplete);
		} catch (InterruptedException e) {
			System.err.println("Incomplete orange processing, juice may be bad");
		}
	}
}