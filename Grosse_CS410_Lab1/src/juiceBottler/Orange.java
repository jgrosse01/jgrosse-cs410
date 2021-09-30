package juiceBottler;

/**
 * @desc the basis for the juice bottler, this is the primary resource for our
 *       bottling plants.
 * @author Nate Williams
 * @commentor Jake Grosse
 *
 */
public class Orange {

	// enumeration containing the progression of an orange.
	public enum State {
		Fetched(15), Peeled(38), Squeezed(29), Bottled(17), Processed(1);

		// last state is Processed
		private static final int finalIndex = State.values().length - 1;

		final int timeToComplete;

		State(int timeToComplete) {
			this.timeToComplete = timeToComplete;
		}

		/**
		 * @desc Method intended to return the state after the current state of the
		 *       orange. Can be used to increment the state of the orange by one. Used
		 *       implicitly when using runProcess()
		 * @return Next State
		 */
		State getNext() {
			int currIndex = this.ordinal();
			// if we surpass the maximum state, thrown an error
			if (currIndex >= finalIndex) {
				throw new IllegalStateException("Already at final state");
			}
			return State.values()[currIndex + 1];
		}
	}

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
	 * @desc processes the orange into the next state
	 */
	public void runProcess() {
		// Don't attempt to process an already completed orange
		if (state == State.Processed) {
			throw new IllegalStateException("This orange has already been processed");
		}
		doWork();
		// implementation of getNext where we increment the state of the orange
		state = state.getNext();
	}

	/**
	 * @desc simply waits for the orange process to complete. Forces a minimum time.
	 */
	private void doWork() {
		// Sleep for the amount of time necessary to do the work
		try {
			Thread.sleep(state.timeToComplete);
		} catch (InterruptedException e) {
			System.err.println("Incomplete orange processing, juice may be bad");
		}
	}
}
