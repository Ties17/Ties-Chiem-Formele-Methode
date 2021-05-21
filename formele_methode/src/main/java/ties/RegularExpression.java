package ties;
import java.util.ArrayList;
import java.util.Arrays;

import ties.RegExOperators.*;

public class RegularExpression {

    // for example = (a|b.c)*
    private String expression;
    private ArrayList<Character> language = new ArrayList<>();
    private ArrayList<Character> operators = new ArrayList<>(Arrays.asList('|', '+', '*', '.', '(', ')'));
    private ArrayList<RegExOperator> ops = new ArrayList<>();

    public RegularExpression(String exp) {
        this.expression = exp;
        filterLanguage();
        ops = createOperators(this.expression);
        System.out.println(ops);
    }

    public void filterLanguage() {
        for (int i = 0; i < this.expression.length(); i++) {
            char c = this.expression.charAt(i);
            if (!operators.contains(c)) {
                language.add(c);
            }
        }
    }

    public ArrayList<RegExOperator> createOperators(String exp){
        ArrayList<RegExOperator> ops = new ArrayList<>();

        for(int i = 0 ; i < exp.length() ; i++){
            char c = exp.charAt(i);

            if(c == '('){
                
            }
        }

        return ops;
    }

    public RegExOperator createOperator(String exp) {
        System.out.println("Creating operator for string: " + exp);
        RegExOperator oper = null;
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            switch (c) {
                case '|':
                    String left = "";
                    for (int lefti = i; lefti > -1; lefti--) {
                        char temp = exp.charAt(lefti);
                        if (!operators.contains(temp)) {
                            left = temp + left;
                        }
                    }
                    String right = "";
                    for (int righti = i; righti < exp.length(); righti++) {
                        char temp = exp.charAt(righti);
                        if (!operators.contains(temp)) {
                            right += temp;
                        }
                    }
                    System.out.println("Found left: " + left + ", right: " + right);
                    oper = new RegExOr(createOperator(left), createOperator(right));
                    break;
                case '+':
                    break;
                case '*':
                    break;
                case '.':
                    break;
                case '(':
                    break;
                default:
                    if (!operators.contains(c)) {
                        oper = new RegExOne(c);
                        System.out.println("Created one operator for: " + c);
                    }
                    break;
            }
        }
        return oper;
    }

    public ArrayList<String> getLanguageForLength(int length) {

        for (int i = 0; i < length; i++) {

        }

        return null;
    }

    public boolean accept(String word) {
        // for (int i = 0; i < word.length(); i++) {
        //     if (!op.accept(word.charAt(i))) {
        //         return false;
        //     }
        // }
        return true;
    }
}
