package juiceBottler;

public class Orange {
	
	protected static enum State {
		//all the states an orange can be
        Fetched(15),
        Peeled(38),
        Juiced(29),
        Bottled(17),
        Finished(1);

		//the final index is the last value in the enum, being Finished(1)
        private static final int finalIndex = State.values().length - 1;
        
        //sets the time it takes to complete one orange
        final int timeToComplete;

        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }

        //gets the next state, throws an error if it is finished
        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }
            return State.values()[currIndex + 1];
        }
    }
	
	public Orange() {
		
	}
}
