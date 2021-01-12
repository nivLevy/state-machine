package app.states;

import stateMachine.State;

public class ThreeInSeqState extends State {

    @Override
    public void onVisit() {
        System.out.println("** The machine received 3 consecutive events **");
    }
}
