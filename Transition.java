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
        return this.fromState.compareTo(o.fromState);
    }

}
