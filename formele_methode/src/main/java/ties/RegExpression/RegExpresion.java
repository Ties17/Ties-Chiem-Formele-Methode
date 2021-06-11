package ties.RegExpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import ties.Machine;
import ties.Transition;
import ties.Type;
import ties.RegExpression.Operators.Operator;

public class RegExpresion {

    public Operator operator;
    public String terminals;

    RegExpresion left;
    RegExpresion right;

    public RegExpresion() {
        operator = Operator.ONE;
        terminals = "";
        left = null;
        right = null;
    }

    public RegExpresion(String p) {
        operator = Operator.ONE;
        terminals = p;
        left = null;
        right = null;
    }

    public RegExpresion plus() {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.PLUS;
        result.left = this;
        return result;
    }

    public RegExpresion star() {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.STAR;
        result.left = this;
        return result;
    }

    public RegExpresion or(RegExpresion e2) {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.OR;
        result.left = this;
        result.right = e2;
        return result;
    }

    public RegExpresion dot(RegExpresion e2) {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.DOT;
        result.left = this;
        result.right = e2;
        return result;
    }

    public SortedSet<String> getLanguage(int maxSteps) {
        RegexCompareByLength regexCompareByLength = new RegexCompareByLength();
        SortedSet<String> emptyLanguage = new TreeSet<String>(regexCompareByLength.compareByLength);
        SortedSet<String> languageResult = new TreeSet<String>(regexCompareByLength.compareByLength);

        SortedSet<String> languageLeft, languageRight;

        if (maxSteps < 1)
            return emptyLanguage;

        switch (this.operator) {
            case ONE:
                languageResult.add(terminals);

            case OR:
                languageLeft = left == null ? emptyLanguage : left.getLanguage(maxSteps - 1);
                languageRight = right == null ? emptyLanguage : right.getLanguage(maxSteps - 1);
                languageResult.addAll(languageLeft);
                languageResult.addAll(languageRight);
                break;

            case DOT:
                languageLeft = left == null ? emptyLanguage : left.getLanguage(maxSteps - 1);
                languageRight = right == null ? emptyLanguage : right.getLanguage(maxSteps - 1);
                for (String s1 : languageLeft)
                    for (String s2 : languageRight) {
                        languageResult.add(s1 + s2);
                    }
                break;

            // STAR(*) en PLUS(+) kunnen we bijna op dezelfde manier uitwerken:
            case STAR:
            case PLUS:
                languageLeft = left == null ? emptyLanguage : left.getLanguage(maxSteps - 1);
                languageResult.addAll(languageLeft);
                for (int i = 1; i < maxSteps; i++) {
                    HashSet<String> languageTemp = new HashSet<String>(languageResult);
                    for (String s1 : languageLeft) {
                        for (String s2 : languageTemp) {
                            languageResult.add(s1 + s2);
                        }
                    }
                }
                if (this.operator == Operator.STAR) {
                    languageResult.add("");
                }
                break;

            default:
                System.out.println("getLanguage is nog niet gedefinieerd voor de operator: " + this.operator);
                break;
        }

        return languageResult;
    }

    public boolean isEqualTo(RegExpresion re){
        return this.getLanguage(15).equals(re.getLanguage(15));
    }

    public Machine<Integer> thompsonConvert() {
        Character[] a = createAlphabetForTerminals(findTerminals(this));

        Machine<Integer> m = new Machine<Integer>(a);

        m.addBeginState(0);

        m.addTransition(new Transition<Integer>(0, this, 1));

        doThompson(m);

        return m;
    }

    public void doThompson(Machine<Integer> m) {
        doThompson(m, new TreeSet<Integer>());
    }

    private void doThompson(Machine<Integer> m, TreeSet<Integer> takenStates) {
        while(!findRegExTransitions(m).isEmpty()){
            
            for (Transition<Integer> trans : findRegExTransitions(m)) {
                takenStates.add(trans.fromState);
                takenStates.add(trans.toState);
                RegExpresion regEx = trans.regEx;

                switch (regEx.operator) {
                    case ONE:
                        if (regEx.terminals.isEmpty()) {
                            m.addTransition(new Transition<Integer>(trans.fromState, Type.EPSILON, trans.toState));
                        } else {
                            int lastState = trans.fromState;
                            for (int i = 0; i < regEx.terminals.length(); i++) {
                                char c = regEx.terminals.charAt(i);
                                if (i == regEx.terminals.length() - 1) {
                                    int nextState = nextState(takenStates);
                                    m.addTransition(new Transition<Integer>(lastState, c, trans.toState));
                                    takenStates.add(nextState);
                                } else {
                                    int nextState = nextState(takenStates);
                                    m.addTransition(new Transition<Integer>(lastState, c, nextState));
                                    takenStates.add(nextState);
                                    lastState = nextState;
                                }
                            }
                        }
                        break;
                    case OR:
                        int left1 = nextState(takenStates);
                        takenStates.add(left1);
                        int left2 = nextState(takenStates);
                        takenStates.add(left2);
                        int right1 = nextState(takenStates);
                        takenStates.add(right1);
                        int right2 = nextState(takenStates);
                        takenStates.add(right2);

                        m.addTransition(new Transition<Integer>(trans.fromState, Type.EPSILON, left1));
                        m.addTransition(new Transition<Integer>(left1, regEx.left, left2));
                        m.addTransition(new Transition<Integer>(left2, Type.EPSILON, trans.toState));

                        m.addTransition(new Transition<Integer>(trans.fromState, Type.EPSILON, right1));
                        m.addTransition(new Transition<Integer>(right1, regEx.right, right2));
                        m.addTransition(new Transition<Integer>(right2, Type.EPSILON, trans.toState));
                        
                        break;
                    case STAR:
                    case PLUS:
                        int begin = nextState(takenStates);
                        takenStates.add(begin);
                        int end = nextState(takenStates);
                        takenStates.add(end);

                        m.addTransition(new Transition<Integer>(trans.fromState, Type.EPSILON, begin));
                        m.addTransition(new Transition<Integer>(begin, regEx.left, end));
                        m.addTransition(new Transition<Integer>(end, Type.EPSILON, begin));
                        m.addTransition(new Transition<Integer>(end, Type.EPSILON, trans.toState));

                        if(regEx.operator == Operator.STAR){
                            m.addTransition(new Transition<Integer>(trans.fromState, Type.EPSILON, trans.toState));
                        }

                        break;
                    case DOT:
                        int middle = nextState(takenStates);
                        m.addTransition(new Transition<Integer>(trans.fromState, regEx.left, middle));
                        m.addTransition(new Transition<Integer>(middle, regEx.right, trans.toState));
                        takenStates.add(middle);
                        break;
                }
                m.transitions.remove(trans);
            }
        }        
    }

    private ArrayList<Transition<Integer>> findRegExTransitions(Machine<Integer> m){
        ArrayList<Transition<Integer>> regExTrans = new ArrayList<>();
        for (Transition<Integer> trans : m.transitions) {
            if (trans.type == Type.REGEXPRESSION) {
                regExTrans.add(trans);
            }
        }
        return regExTrans;
    }

    private int nextState(TreeSet<Integer> takenStates) {
        int highest = Integer.MIN_VALUE;
        for (int i : takenStates) {
            if (i > highest) {
                highest = i;
            }
        }
        return highest + 1;
    }

    public ArrayList<RegExpresion> nextRegEx(RegExpresion regEx) {
        ArrayList<RegExpresion> regexs = new ArrayList<>();

        if (regEx.left != null) {
            regexs.add(regEx.left);
        }
        if (regEx.right != null) {
            regexs.add(regEx.right);
        }

        return regexs;
    }

    public Character[] createAlphabetForTerminals(SortedSet<String> terminals) {
        TreeSet<Character> sortedAlphabet = new TreeSet<>();

        for (String s : terminals) {
            for (char c : s.toCharArray()) {
                sortedAlphabet.add(c);
            }
        }

        Character[] alphabet = new Character[sortedAlphabet.size()];
        if (sortedAlphabet.size() > 0) {
            for (int i = 0; i < sortedAlphabet.size(); i++) {
                alphabet[i] = (Character) sortedAlphabet.toArray()[i];
            }
        }

        return alphabet;
    }

    public SortedSet<String> findTerminals(RegExpresion reg) {
        SortedSet<String> terminals = new TreeSet<>();

        terminals.add(reg.terminals);

        if (reg.left != null) {
            terminals.addAll(findTerminals(reg.left));
        }
        if (reg.right != null) {
            terminals.addAll(findTerminals(reg.right));
        }

        return terminals;
    }
}
