package ties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

public class Machine<T extends Comparable<T>> {
    
    ArrayList<Character> alphabet;
    ArrayList<Transition<T>> transitions = new ArrayList<>();
    T beginState, endState;
    
    public Machine(Character[] alphabet){
        ArrayList<Character> al = new ArrayList<>();
        for (Character character : alphabet) {
            al.add(character);
        }
        this.alphabet = al;

        System.out.println("Machine made with alphabet: ");
        for (Character character : alphabet) {
            System.out.println(character);
        }
    }

    public void addTransition(Transition<T> trans){
        transitions.add(trans);
        Collections.sort(transitions);
        // System.out.println(transitions);
    }

    public boolean accept(String word){
        for(int i = 0; i < word.length(); i ++){
            char s = word.charAt(i);
            
            if(!alphabet.contains(s)){
                return false;
            }
        }
        // check transities voor accepteren
        // T startState = transitions.get(0).fromState;
        
        T state = beginState;
        for(int i = 0 ; i < word.length(); i++){
            Character s = word.charAt(i);

            ArrayList<Transition<T>> transits = findTransitionWithStartState(state);
            // CASE DFA
            for(Transition<T> trans : transits){
                if(trans.acceptor == s){
                    state = trans.toState;
                }
            }
            // if (trans.acceptor == s) {
            //     state = trans.toState;
            // } else {
            //     state = transitions.get(0).fromState;
            // }
            
        }

        return state == endState;
    }

    public ArrayList<String> getLanguageForLength(int length){
        ArrayList<String> possibleWords = new ArrayList<String>();

        for (int index = 0; index < alphabet.size(); index++) {
            String word = "";
            word += alphabet.get(index);
            possibleWords.add(word);
        }

        for(int amount = 2; amount <= length ; amount++){
            ArrayList<String> temp = new ArrayList<>();
            for(String word : possibleWords){
                if(word.length() == amount-1){
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
        for(String word : possibleWords){
            if(accept(word) == true){
                words.add(word);
            }
        }

        return words;
    }

    private ArrayList<Transition<T>> findTransitionWithStartState(T startState){
        ArrayList<Transition<T>> transits = new ArrayList<>();
        //System.out.println("Start finding");
        for(Transition<T> trans : transitions){
            //System.out.println("Comparing: " + trans.fromState + " & " + startState);
            if(trans.fromState.equals(startState)){
                transits.add(trans);
            }
        }
        return transits;
    }
}
