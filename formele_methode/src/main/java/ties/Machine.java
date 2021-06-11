package ties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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

    public Machine(ArrayList<Character> alphabet) {
        this.alphabet = alphabet;
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

    public ArrayList<T> findEndStates(T startState, char acceptor) {
        HashSet<T> states = new HashSet<>();

        for (T state : epsilonClosure(startState)) {
            for (Transition<T> t : findTransition(state, acceptor)) {
                states.add(t.toState);
                states.addAll(epsilonClosure(t.toState));
            }
        }

        ArrayList<T> s = new ArrayList<>();
        Iterator<T> t = states.iterator();
        while (t.hasNext()) {
            s.add(t.next());
        }

        Collections.sort(s);
        //System.out.println("Found endStates for startState: " + startState + " & acceptor: " + acceptor + " = " + s);
        return s;
    }

    public ArrayList<T> findEndStates(ArrayList<T> stateList, char acceptor) {
        HashSet<T> states = new HashSet<>();

        for(T startState : stateList){
            states.addAll(findEndStates(startState, acceptor));
        }

        ArrayList<T> s = new ArrayList<>();
        Iterator<T> t = states.iterator();
        while (t.hasNext()) {
            s.add(t.next());
        }

        Collections.sort(s);
        //System.out.println("Found endStates for stateList: " + stateList + " & acceptor: " + acceptor + " = " + s);
        return s;
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

    public TreeSet<T> epsilonClosure(T state) {
        // WIP mogelijk bugs aanwezig nog niet volledig door getest
        ArrayList<T> tempClosure = new ArrayList<>();

        ArrayList<Transition<T>> transitions = findEpsilonTransitionWithStartState(state);

        for (Transition<T> trans : transitions) {
            tempClosure.add(trans.toState);
        }

        TreeSet<T> closure = new TreeSet<>();

        for (T s1 : tempClosure) {
            closure.addAll(epsilonClosure(s1));
        }

        closure.addAll(tempClosure);
        closure.add(state);

        return closure;
    }

    public void draw() {
        GraphizGenerator<T> g = new GraphizGenerator<>(this);
        g.GenerateImage("diagram");
    }

    public void drawName(String fileName) {
        GraphizGenerator<T> g = new GraphizGenerator<>(this);
        g.GenerateImage(fileName);
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

    public Machine<String> toDFA() {
        if (!this.isDFA()) {

            HashSet<ArrayList<T>> newStates = new HashSet<>();
            ArrayList<ArrayList<T>> startStates = new ArrayList<>();
            for(T start : beginStates){
                ArrayList<T> arr = new ArrayList<>();
                arr.add(start);
                newStates.add(arr);
                startStates.add(arr);
            }
            for (T state : getStates()) {
                for (char c : alphabet) {
                    newStates.add(findEndStates(state, c));
                }
            }
            System.out.println(newStates);

            Machine<String> m = new Machine<String>(alphabet);

            for(ArrayList<T> state : newStates){
                for (char c : alphabet) {
                    Transition<String> t = new Transition<String>(state.toString(), c, findEndStates(state, c).toString());
                    //System.out.println(t);
                    m.addTransition(t);
                }
                for(T end : endStates){
                    if(state.contains(end)){
                        m.addEndState(state.toString());
                    }
                }
            }

            for(ArrayList<T> s : startStates){
                m.addBeginState(s.toString());
            }
            return m;
        } else {
            return null;
        }
    }

    public Machine<T> reverse(){
       Machine<T> m = new Machine<>(alphabet);
       
       for(T state : beginStates){
           m.addEndState(state);
       }
       for(T state : endStates){
           m.addBeginState(state);
       }
       for(Transition<T> trans : transitions){
           m.addTransition(new Transition<T>(trans.toState, trans.acceptor, trans.fromState));
       }

       return m;
    }

}
