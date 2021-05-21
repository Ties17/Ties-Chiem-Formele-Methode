package ties.RegExOperators;

public class RegExOr implements RegExOperator{
    RegExOperator left;
    RegExOperator right;

    public RegExOr(RegExOperator left, RegExOperator right){
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean accept(char key) {
        return left.accept(key) || right.accept(key);
    }
}
