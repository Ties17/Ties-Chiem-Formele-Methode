package ties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

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

    public void removeTransition(Transition<T> trans) {
        transitions.remove(trans);
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

            ArrayList<Transition<T>> transits = findTransition(startState);
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

    private ArrayList<Transition<T>> findTransition(T startState) {
        ArrayList<Transition<T>> transits = new ArrayList<>();
        for (Transition<T> trans : transitions) {
            if (trans.fromState.equals(startState)) {
                transits.add(trans);
            }
        }
        return transits;
    }

    private ArrayList<Transition<T>> findTransitionToState(T toState) {
        ArrayList<Transition<T>> transits = new ArrayList<>();
        for (Transition<T> trans : transitions) {
            if (trans.toState.equals(toState)) {
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
        // System.out.println("Found endStates for startState: " + startState + " &
        // acceptor: " + acceptor + " = " + s);
        return s;
    }

    public ArrayList<T> findEndStates(ArrayList<T> stateList, char acceptor) {
        HashSet<T> states = new HashSet<>();

        for (T startState : stateList) {
            states.addAll(findEndStates(startState, acceptor));
        }

        ArrayList<T> s = new ArrayList<>();
        Iterator<T> t = states.iterator();
        while (t.hasNext()) {
            s.add(t.next());
        }

        Collections.sort(s);
        // System.out.println("Found endStates for stateList: " + stateList + " &
        // acceptor: " + acceptor + " = " + s);
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
            for (T start : beginStates) {
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

            HashSet<ArrayList<T>> lastStates = new HashSet<>();
            while (lastStates.size() != newStates.size()) {
                lastStates = (HashSet<ArrayList<T>>) newStates.clone();
                for (ArrayList<T> state : lastStates) {
                    for (char c : alphabet) {
                        newStates.add(findEndStates(state, c));
                    }
                }
            }

            System.out.println(newStates);
            Machine<String> m = new Machine<String>(alphabet);

            for (ArrayList<T> state : newStates) {
                for (char c : alphabet) {
                    Transition<String> t = new Transition<String>(state.toString(), c,
                            findEndStates(state, c).toString());
                    // System.out.println(t);
                    m.addTransition(t);
                }
                for (T end : endStates) {
                    if (state.contains(end)) {
                        m.addEndState(state.toString());
                    }
                }
            }

            for (ArrayList<T> s : startStates) {
                m.addBeginState(s.toString());
            }
            return m;
        } else {
            return null;
        }
    }

    public Machine<T> reverse() {
        Machine<T> m = new Machine<>(alphabet);

        for (T state : beginStates) {
            m.addEndState(state);
        }
        for (T state : endStates) {
            m.addBeginState(state);
        }
        for (Transition<T> trans : transitions) {
            m.addTransition(new Transition<T>(trans.toState, trans.acceptor, trans.fromState));
        }

        return m;
    }

    public Machine<Integer> renameStates() {
        takenTokens.clear();
        HashMap<T, Integer> translationTable = new HashMap<>();
        for (T state : getStates()) {
            translationTable.put(state, getNextToken());
        }

        Machine<Integer> m = new Machine<>(alphabet);
        for (Transition<T> trans : transitions) {
            m.addTransition(new Transition<Integer>(translationTable.get(trans.fromState), trans.acceptor,
                    translationTable.get(trans.toState)));
        }
        for(T state : beginStates){
            m.addBeginState(translationTable.get(state));
        }
        for (T state : endStates) {
            m.addEndState(translationTable.get(state));
        }
        return m;
    }

    public Machine<Character> minimize() {
        HashMap<Character, SortedSet<T>> states = new HashMap<>();
        SortedSet<T> statesWithoutEndstates = getStates();
        statesWithoutEndstates.removeAll(endStates);
        states.put((char) getNextToken(), statesWithoutEndstates);
        states.put((char) getNextToken(), endStates);

        HashMap<Character, SortedSet<T>> lastStates = null;
        while (!states.equals(lastStates) || lastStates == null) {
            lastStates = states;
            states = splitUp(states);
        }

        System.out.println(states);

        Machine<Character> m = new Machine<>(alphabet);

        for (Entry<Character, SortedSet<T>> e : states.entrySet()) {
            for (char c : alphabet) {
                Transition<T> trans = findTransition(e.getValue().first(), c).get(0);
                m.addTransition(new Transition<Character>(e.getKey(), trans.acceptor, findKey(states, trans.toState)));
            }
        }
        for (T state : beginStates) {
            m.addBeginState(findKey(states, state));
        }
        for (T state : endStates) {
            m.addEndState(findKey(states, state));
        }

        return m;
    }

    public HashMap<Character, SortedSet<T>> splitUp(HashMap<Character, SortedSet<T>> states) {
        // System.out.println("Splitting: " + states);
        HashMap<String, SortedSet<T>> tempStates = new HashMap<>();

        for (Entry<Character, SortedSet<T>> e : states.entrySet()) {
            for (T state : e.getValue()) {
                String tempKey = "";
                if (endStates.contains(state)) {
                    tempKey += "*";
                }
                for (char c : alphabet) {
                    T toState = findTransition(state, c).get(0).toState;
                    tempKey += findKey(states, toState);
                }

                SortedSet<T> set = tempStates.get(tempKey);
                if (set == null) {
                    SortedSet<T> tempSet = new TreeSet<>();
                    tempSet.add(state);
                    tempStates.put(tempKey, tempSet);
                } else {
                    set.add(state);
                    tempStates.put(tempKey, set);
                }
            }
        }
        // System.out.println("Temp: " + tempStates);

        boolean check = false;
        for (Entry<String, SortedSet<T>> e1 : tempStates.entrySet()) {
            check = false;
            for (Entry<Character, SortedSet<T>> e2 : states.entrySet()) {
                if (e1.getValue().equals(e2.getValue())) {
                    check = true;
                }
            }
            if (check == false) {
                break;
            }
        }
        if (check) {
            return states;
        }

        HashMap<Character, SortedSet<T>> newStates = new HashMap<>();
        for (Entry<String, SortedSet<T>> e : tempStates.entrySet()) {
            newStates.put((char) getNextToken(), e.getValue());
        }

        // System.out.println(newStates);

        return newStates;
    }

    public Character findKey(HashMap<Character, SortedSet<T>> states, T toState) {
        for (Entry<Character, SortedSet<T>> e : states.entrySet()) {
            for (T state : e.getValue()) {
                if (state == toState) {
                    return e.getKey();
                }
            }
        }
        return null;
    }

    private ArrayList<Integer> takenTokens = new ArrayList<>();

    public int getNextToken() {
        int newToken;
        if (takenTokens.isEmpty()) {
            newToken = 65;
        } else {
            newToken = Collections.max(takenTokens) + 1;
        }

        takenTokens.add(newToken);
        return newToken;
    }

    private boolean containsDuplicate(ArrayList<ArrayList<T>> list, ArrayList<T> states) {
        for (ArrayList<T> l : list) {
            if (l.containsAll(states) && l.size() == states.size()) {
                return true;
            }
        }

        return false;
    }

}
