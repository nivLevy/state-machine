package app;


import app.states.*;
import stateMachine.Event;
import stateMachine.Machine;
import stateMachine.State;
import stateMachine.StateMachineException;

import java.nio.file.Path;
import java.util.Arrays;

public class App {

    public static void main(String[] args) {

        //create events
        Event eventB = new Event<>(ThreeInSequenceEventTypes.b);
        Event eventA = new Event<>(ThreeInSequenceEventTypes.a);

        //create states
        State start = new StartState();
        State firstA = new FirstAState();
        State secA = new SecAState();
        State firstB = new FirstBState();
        State secB = new SecBState();
        State threeInSeq = new ThreeInSeqState();
        State end = new EndState();

        //create file
        Path file = Path.of("state.txt");

        //create machine
        Machine machine = null;
        try {
            machine = new Machine(file, Arrays.asList(start, firstA, secA, firstB, secB, threeInSeq, end));
        } catch (StateMachineException e) {
            e.printStackTrace();
        }

        //configure transitions
        machine.setTransition(start, eventA, firstA);
        machine.setTransition(start, eventB, firstB);

        machine.setTransition(firstA, eventA, secA);
        machine.setTransition(firstA, eventB, firstB);

        machine.setTransition(secA, eventA, threeInSeq);
        machine.setTransition(secA, eventB, firstB);

        machine.setTransition(firstB, eventB, secB);
        machine.setTransition(firstB, eventA, firstA);

        machine.setTransition(secB, eventA, firstA);
        machine.setTransition(secB, eventB, threeInSeq);

        machine.setTransition(threeInSeq, eventA, end);
        machine.setTransition(threeInSeq, eventB, end);

        //start the machine
        try {
            machine.start(start);
//            machine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        machine.runEvents(Arrays.asList(eventA, eventB, eventA, eventA, eventA));

        //another way to run events, one by one:
//        machine.runEvent(eventA);
    }
}
