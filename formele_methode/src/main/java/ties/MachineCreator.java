package ties;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class MachineCreator {

    public static Machine<Integer> startsWith(String s, Character[] alphabet) {
        Machine<Integer> m = new Machine<>(alphabet);
        m.addBeginState(0);

        int endstate = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            m.addTransition(new Transition<Integer>(i, c, i + 1));

            for (char character : createAlphabetWithoutChar(c, alphabet)) {
                m.addTransition(new Transition<Integer>(i, character, -1));
            }

            endstate = i + 1;
        }

        m.addEndState(endstate);
        m.addTransition(createTransitionsForState(endstate, alphabet));

        return m;
    }

    public static Machine<Integer> endsWith(String s, Character[] alphabet) {
        Machine<Integer> m = new Machine<>(alphabet);

        m.addBeginState(0);

        boolean isFirstChar = true;
        char lastChar = s.charAt(0);
        int endstate = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (lastChar != c && isFirstChar) {
                m.addTransition(new Transition<Integer>(i, lastChar, i));
                isFirstChar = false;
            }

            m.addTransition(new Transition<Integer>(i, c, i + 1));
            for (char character : createAlphabetWithoutChar(c, alphabet)) {
                m.addTransition(new Transition<Integer>(i, character, 0));
            }

            lastChar = c;
            endstate = i + 1;
        }

        m.addEndState(endstate);
        ArrayList<Pair<Integer, Character>> patterns = findPatterns(s);
        if (patterns.isEmpty()) {
            for (char c : alphabet) {
                m.addTransition(new Transition<Integer>(endstate, c, 0));
            }
        } else {
            for (Pair<Integer, Character> pair : patterns) {
                m.addTransition(new Transition<Integer>(endstate, pair.value, endstate - pair.key + 1));
            }
            // System.out.println(createRemainingCharList(patterns, alphabet));
            for (Character c : createRemainingCharList(patterns, alphabet)) {
                m.addTransition(new Transition<Integer>(endstate, c, 0));
            }
        }

        return m;
    }

    public static Machine<Integer> contains(String s, Character[] alphabet) {
        Machine<Integer> m = new Machine<>(alphabet);

        m.addBeginState(0);

        int endstate = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            m.addTransition(new Transition<Integer>(i, c, i + 1));

            for (char character : createAlphabetWithoutChar(c, alphabet)) {
                m.addTransition(new Transition<Integer>(i, character, 0));
            }

            endstate = i + 1;
        }

        m.addEndState(endstate);
        m.addTransition(createTransitionsForState(endstate, alphabet));

        return m;
    }

    private static ArrayList<Transition<Integer>> createTransitionsForState(int state, Character[] alphabet) {
        ArrayList<Transition<Integer>> transits = new ArrayList<>();

        for (char c : alphabet) {
            transits.add(new Transition<Integer>(state, c, state));
        }

        return transits;
    }

    private static ArrayList<Character> createAlphabetWithoutChar(Character c, Character[] alphabet) {
        ArrayList<Character> chars = new ArrayList<>();

        for (char s : alphabet) {
            if (s != c) {
                chars.add(s);
            }
        }

        return chars;
    }

    private static ArrayList<Character> createAlphabetWithoutChars(ArrayList<Character> c, Character[] alphabet) {
        ArrayList<Character> chars = new ArrayList<>();

        for (char s : alphabet) {
            for (char t : c) {
                if (s != t) {
                    chars.add(s);
                }
            }

        }

        return chars;
    }

    private static ArrayList<Character> createRemainingCharList(ArrayList<Pair<Integer, Character>> pairs,
            Character[] alphabet) {
        ArrayList<Character> charsInPairs = new ArrayList<>();

        for (Pair<Integer, Character> pair : pairs) {
            charsInPairs.add(pair.value);
        }

        System.out.println(charsInPairs);

        return createAlphabetWithoutChars(charsInPairs, alphabet);
    }

    public static ArrayList<Pair<Integer, Character>> findPatterns(String s) {
        ArrayList<Pair<Integer, Character>> vals = new ArrayList<>();

        for (int size = 2; size <= s.length() / 2; size++) {
            Pair<Integer, Character> e = hasPattern(s, size);
            if (e != null && !isMultiple(e.key, vals)) {
                vals.add(e);
            }
        }

        return vals;
    }

    private static boolean isMultiple(int val, ArrayList<Pair<Integer, Character>> arr) {
        for (Pair<Integer, Character> pair : arr) {
            if (val % pair.key == 0) {
                return true;
            }
        }

        return false;
    }

    private static Pair<Integer, Character> hasPattern(String s, int length) {

        if (length > s.length() / 2) {
            return null;
        }

        String pattern = s.substring(s.length() - length, s.length());

        int amount = (s.length() / length) - 1;
        int res = s.length() % length;

        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            arr.add(s.substring(res + (i * length), res + (i * length + length)));
        }
        for (String st : arr) {
            System.out.print(st + " - ");
        }
        System.out.println();
        for (String st : arr) {
            if (!st.equals(pattern)) {
                return null;
            }
        }

        String resPattern = s.substring(s.length() - res, s.length());
        String start = s.substring(0, res);

        if (!start.equals(resPattern)) {
            return null;
        }

        return new Pair(length, pattern.substring(0, 1).toCharArray()[0]);
    }
}
