package ties;

public class Transition<T extends Comparable<T>> implements Comparable<Transition<T>> {

    public T fromState;
    public char acceptor;
    public T toState;
    public Type type;

    public Transition(T fromState, char s, T toState){
        this.fromState = fromState;
        this.acceptor = s;
        this.toState = toState;
        this.type = Type.CHARACTER;
    }

    public Transition(T fromState, Type s, T toState) {
        this.fromState = fromState;
        this.toState = toState;
        if (s != null){
            this.type = s;
        } else {
            this.type = Type.EPSILON;
        }
        
    }

    public char getAcceptorChar(){
        switch(this.type){
            case EPSILON:
                return 'ε';
            default:
                return this.acceptor;
        }
    }

    public boolean accept(char c){
        switch(this.type){
            case EPSILON:
                return true;
            case CHARACTER:
                return this.acceptor == c;
        }
        return true;
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
        switch (this.type) {
            case EPSILON:
                return fromState.toString() + " : " + 'ε' + " : " + toState.toString();
            default:
                return fromState.toString() + " : " + acceptor + " : " + toState.toString();
        }

        
    }
}
