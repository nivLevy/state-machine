package stateMachine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine {

    private Path filePath;
    private Map<Integer, State> statesHashes; //map -hash of class of state- to -state-
    private State currState;
    private Map<State, Map<Event, State>> transitions; //map state to (map -event- to -state-)


    public Machine(Path filePath, List<State> states) throws StateMachineException {
        this.filePath = filePath;
        initStatesHashes(states);
        initTransitions(states);
    }

    /**
     * start the machine from startState
     * @param startState startState
     */
    public void start(State startState) {
        currState = startState;
        setStateOnFile(currState);
    }

    /**
     * start the machine from the state in the file
     * @throws Exception exception
     */
    public void start() throws Exception {
        currState = getStateFromFile();
    }

    private void initStatesHashes(List<State> states) throws StateMachineException {
        statesHashes = new HashMap<>(states.size());
        for (State state : states) {
            if (statesHashes.containsKey(state.getClass().hashCode())) { //if the state class is already exist we throw an error, Every state should has its own class
                throw new StateMachineException("Every state should has its own class");
            }
            statesHashes.put(state.getClass().hashCode(), state);
        }
    }

    private void initTransitions(List<State> states) {
        transitions = new HashMap<>(states.size());
        for (State state : states) {
            transitions.put(state, new HashMap<>(states.size()));
        }
    }

    public void setTransition(State state, Event event, State nextState) {
        transitions.get(state).put(event, nextState);
    }

    public void runEvent(Event event) {
        State nextState = transitions.get(currState).get(event);
        if (nextState != null) { //if the client didn't define a transition, we stay in the same state
            currState = nextState;
        }
        currState.onVisit();
        setStateOnFile(currState);
    }

    public void runEvents(List<Event> events) {
        for (Event event : events) {
            runEvent(event);
        }
    }

    private void setStateOnFile(State state){
        try {
            Files.writeString(filePath, String.valueOf(state.getClass().hashCode())); //we save the hash of class of the state
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private State getStateFromFile() throws Exception {
        String hashStr = Files.readString(filePath);
        State state = statesHashes.get(Integer.valueOf(hashStr));
        if (state != null) {
            return state;
        }
        throw new StateMachineException("couldn't get state from file");
    }
}
