package ties;

public class Transition<T extends Comparable<T>> implements Comparable<Transition<T>> {
    
    public T fromState;
    public char acceptor;
    public T toState;

    public Transition(T fromState, char s, T toState){
        this.fromState = fromState;
        this.acceptor = s;
        this.toState = toState;
    }

    @Override
    public int compareTo(Transition<T> o) {
        if (this.fromState.compareTo(o.fromState) != 0){
            return this.fromState.compareTo(o.fromState);
        } else {
            return this.toState.compareTo(o.toState);
        }
    }

    @Override
    public String toString() {
        return fromState.toString() + " : " + acceptor + " : " + toState.toString();
    }
}
