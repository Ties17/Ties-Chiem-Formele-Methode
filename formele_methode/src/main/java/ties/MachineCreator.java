package ties;
import java.util.ArrayList;

public class MachineCreator {
    
    public static Machine<Integer> startsWith(String s, Character[] alphabet){
        Machine<Integer> m = new Machine<>(alphabet);
        m.addBeginState(0);

        int endstate = 0;
        for(int i = 0 ; i < s.length() ; i++){
            char c = s.charAt(i);

            m.addTransition(new Transition<Integer>(i, c, i + 1));
            
            for(char character : createAlphabetWithoutChar(c, alphabet)){
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
       

        int endstate = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            m.addTransition(new Transition<Integer>(i, c, i + 1));

            

            endstate = i + 1;
        }

        m.addEndState(endstate);

        return m;
    }

    public static Machine<Integer> contains(String s, Character[] alphabet) {
        Machine<Integer> m = new Machine<>(alphabet);

        return m;
    }

    private static ArrayList<Transition<Integer>> createTransitionsForState(int state, Character[] alphabet){
        ArrayList<Transition<Integer>> transits = new ArrayList<>();

        for(char c : alphabet){
            transits.add(new Transition<Integer>(state, c, state));
        }

        return transits;
    }

    private static ArrayList<Character> createAlphabetWithoutChar(Character c, Character[] alphabet){
        ArrayList<Character> chars = new ArrayList<>();

        for(char s : alphabet){
            if(s != c){
                chars.add(s);
            }
        }

        return chars;
    }
}
