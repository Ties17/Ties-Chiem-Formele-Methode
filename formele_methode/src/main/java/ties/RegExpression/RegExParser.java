package ties.RegExpression;

import ties.RegExpression.Operators.Operator;

public class RegExParser {
    public RegExParser() {

    }

    public RegExpresion parseString(String regex) {
        // System.out.println("Parsing regex: " + regex);
        RegExpresion top = null;

        char c = regex.charAt(0);
        int startingPos = 1;

        switch (c) {
            case '*':
                return null;
            case '+':
                return null;
            case '|':
                return null;
            case '(':
                int indexOfBrace = findEndBraceIndex(regex.substring(1));
                top = parseString(regex.substring(1, indexOfBrace + 1));
                startingPos = indexOfBrace + 1;
                break;
            case ')':
                break;
            default:
                top = new RegExpresion("" + c);
                break;
        }

        for (int i = startingPos; i < regex.length(); i++) {
            char cc = regex.charAt(i);
            switch (cc) {
                case '*':
                    top = top.star();
                    break;
                case '+':
                    top = top.plus();
                    break;
                case '|':
                    top = top.or(null);
                    break;
                case '(':
                    int indexOfBrace = findEndBraceIndex(regex.substring(i + 1));
                    RegExpresion re = parseString(regex.substring(i + 1, indexOfBrace + i + 1));

                    if (top.operator == Operator.OR || top.operator == Operator.DOT) {
                        top.right = re;
                    } else {
                        top = re;
                    }

                    i = i + indexOfBrace;
                    break;
                case ')':
                    break;
                default:
                    switch (top.operator) {
                        case ONE:
                        case DOT:
                            top = top.dot(new RegExpresion("" + cc));
                            break;
                        case OR:
                            top.right = new RegExpresion("" + cc);
                        default:
                            break;
                    }
                    break;
            }
        }
        return top;
    }

    private int findEndBraceIndex(String regex) {
        // System.out.println("Finding end brace of: " + regex);
        int endBrace = regex.indexOf(')');
        int startBrace = regex.indexOf('(');

        while (startBrace < endBrace) {
            if (startBrace == -1) {
                break;
            }
            endBrace = regex.indexOf(')', endBrace + 1);
            startBrace = regex.indexOf('(', startBrace + 1);
        }
        // System.out.println("Found ')' at: " + endBrace);
        return endBrace;
    }
}
