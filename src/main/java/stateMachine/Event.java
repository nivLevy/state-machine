package stateMachine;

public class Event<T>{

    private T type;

    public Event(T type) {
        this.type = type;
    }

    public T getType() {
        return this.type;
    }
}
