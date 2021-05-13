package RegExOperators;

public class RegExOne implements RegExOperator{
    private char acceptor;

    public RegExOne(char c){
        this.acceptor = c;
    }

    @Override
    public boolean accept(char key) {
        return key == this.acceptor;
    }
}
