package ties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.RegEx;

import ties.RegExpression.RegExpresion;

public class Machine<T extends Comparable<T>> {

    ArrayList<Character> alphabet;
    public ArrayList<Transition<T>> transitions = new ArrayList<>();
    SortedSet<T> beginStates = new TreeSet<>();
    SortedSet<T> endStates = new TreeSet<>();

    public Machine(Character[] alphabet) {
        ArrayList<Character> al = new ArrayList<>();
        for (Character character : alphabet) {
            al.add(character);
        }
        this.alphabet = al;
    }

    public void addBeginState(T state) {
        beginStates.add(state);
    }

    public void addEndState(T state) {
        endStates.add(state);
    }

    public void addTransition(Transition<T> trans) {
        transitions.add(trans);
        Collections.sort(transitions);
        // System.out.println(transitions);
    }

    public void addTransition(ArrayList<Transition<T>> transits) {
        transitions.addAll(transits);
        Collections.sort(transitions);
    }

    public boolean accept(String word) {
        for (int i = 0; i < word.length(); i++) {
            char s = word.charAt(i);

            if (!alphabet.contains(s)) {
                return false;
            }
        }

        ArrayList<T> endStates = new ArrayList<>();
        for (T startState : this.beginStates) {
            endStates.addAll(acceptNDFArecursive(word, startState));
        }

        for (T endState : endStates) {
            // System.out.println(endState);
            if (this.endStates.contains(endState)) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<T> acceptNDFArecursive(String word, T startState) {
        // System.out.println("NDFA recursive for word: " + word + " & startState: " +
        // startState);
        ArrayList<T> endStates = new ArrayList<>();

        if (word.isEmpty()) {
            // System.out.println("Word is empty");
            endStates.addAll(epsilonClosure(startState));
            endStates.add(startState);
        } else {
            Character s = word.charAt(0);

            ArrayList<Transition<T>> transits = findTransitionWithStartState(startState);
            boolean transFound = false;

            for (Transition<T> trans : transits) {
                if (trans.accept(s)) {
                    transFound = true;
                    // System.out.println(trans);
                    endStates.addAll(acceptNDFArecursive(word.substring(1), trans.toState));
                }
            }

            if (!transFound) {
                // System.out.println("No transition found");
                endStates.addAll(acceptNDFArecursive(word.substring(1), startState));
            }

        }

        return endStates;
    }

    public ArrayList<String> getLanguageForLength(int length) {
        ArrayList<String> possibleWords = new ArrayList<String>();

        for (int index = 0; index < alphabet.size(); index++) {
            String word = "";
            word += alphabet.get(index);
            possibleWords.add(word);
        }

        for (int amount = 2; amount <= length; amount++) {
            ArrayList<String> temp = new ArrayList<>();
            for (String word : possibleWords) {
                if (word.length() == amount - 1) {
                    for (int index = 0; index < alphabet.size(); index++) {
                        String newWord = word;
                        newWord += alphabet.get(index);
                        temp.add(newWord);
                    }
                }
            }
            possibleWords.addAll(temp);
        }

        ArrayList<String> words = new ArrayList<>();
        for (String word : possibleWords) {
            if (accept(word) == true) {
                words.add(word);
            }
        }

        return words;
    }

    private ArrayList<Transition<T>> findTransitionWithStartState(T startState) {
        ArrayList<Transition<T>> transits = new ArrayList<>();
        for (Transition<T> trans : transitions) {
            if (trans.fromState.equals(startState)) {
                transits.add(trans);
            }
        }
        return transits;
    }

    private ArrayList<Transition<T>> findTransition(T startState, char acceptor) {
        ArrayList<Transition<T>> transits = new ArrayList<>();
        for (Transition<T> trans : transitions) {
            if (trans.fromState.equals(startState) && trans.acceptor == acceptor) {
                transits.add(trans);
            }
        }
        return transits;
    }

    private ArrayList<Transition<T>> findEpsilonTransitionWithStartState(T startState) {
        ArrayList<Transition<T>> transits = new ArrayList<>();
        for (Transition<T> trans : transitions) {
            if (trans.fromState.equals(startState) && trans.type == Type.EPSILON) {
                transits.add(trans);
            }
        }
        return transits;
    }

    private ArrayList<T> epsilonClosure(T state) {
        // WIP mogelijk bugs aanwezig nog niet volledig door getest
        ArrayList<T> tempClosure = new ArrayList<>();

        ArrayList<Transition<T>> transitions = findEpsilonTransitionWithStartState(state);

        for (Transition<T> trans : transitions) {
            tempClosure.add(trans.toState);
        }

        ArrayList<T> closure = new ArrayList<>();

        for (T s1 : tempClosure) {
            closure.addAll(epsilonClosure(s1));
        }

        closure.addAll(tempClosure);

        return closure;
    }

    public void draw() {
        new GraphizGenerator<>(this);
    }

    public SortedSet<T> getStates() {
        SortedSet<T> states = new TreeSet<>();
        for (Transition<T> t : transitions) {
            states.add(t.fromState);
            states.add(t.toState);
        }
        return states;
    }

    public boolean isDFA() {
        boolean isDFA = true;

        for (T state : getStates()) {
            for (char c : alphabet) {
                if (findTransition(state, c).size() != 1) {
                    isDFA = false;
                }
            }
        }

        return isDFA;
    }

}
