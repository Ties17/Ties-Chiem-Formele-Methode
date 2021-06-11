package ties;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class MachineTest {

    @Test
    public void MachineSingleEndState() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(0);

        m.addTransition(new Transition<Integer>(0, 'a', 0));
        m.addTransition(new Transition<Integer>(0, 'b', 1));

        m.addTransition(new Transition<Integer>(1, 'a', 1));
        m.addTransition(new Transition<Integer>(1, 'b', 0));

        m.draw();
        assertTrue(m.accept("bab"));
    }

    @Test
    public void MachineMultipleEndStates() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(1);
        m.addEndState(3);

        m.addTransition(new Transition<Integer>(0, 'a', 1));
        m.addTransition(new Transition<Integer>(0, 'b', 0));

        m.addTransition(new Transition<Integer>(1, 'a', 1));
        m.addTransition(new Transition<Integer>(1, 'b', 2));

        m.addTransition(new Transition<Integer>(2, 'a', 2));
        m.addTransition(new Transition<Integer>(2, 'b', 3));

        m.addTransition(new Transition<Integer>(3, 'a', 0));
        m.addTransition(new Transition<Integer>(3, 'b', 3));

        new GraphizGenerator<>(m);

        assertTrue(m.accept("abb"));
    }

    @Test
    public void MachineNDFA() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(0);

        m.addTransition(new Transition<Integer>(0, 'a', 1));

        m.addTransition(new Transition<Integer>(1, 'b', 2));
        m.addTransition(new Transition<Integer>(1, 'b', 3));
        m.addTransition(new Transition<Integer>(1, 'a', 4));

        m.addTransition(new Transition<Integer>(2, 'a', 0));

        m.addTransition(new Transition<Integer>(3, 'a', 4));

        m.addTransition(new Transition<Integer>(4, 'a', 2));

        new GraphizGenerator<>(m);

        assertTrue(m.accept("aaaa"));
    }

    @Test
    public void MachineNDFAMultipleStarts() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addBeginState(3);
        m.addEndState(0);

        m.addTransition(new Transition<Integer>(0, 'a', 1));

        m.addTransition(new Transition<Integer>(1, 'b', 2));
        m.addTransition(new Transition<Integer>(1, 'b', 3));
        m.addTransition(new Transition<Integer>(1, 'a', 4));

        m.addTransition(new Transition<Integer>(2, 'a', 0));

        m.addTransition(new Transition<Integer>(3, 'a', 4));

        m.addTransition(new Transition<Integer>(4, 'a', 2));

        new GraphizGenerator<>(m);

        assertTrue(m.accept("aaa"));
    }

    @Test
    public void MachineNDFAMultipleEnds() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(0);
        m.addEndState(2);

        m.addTransition(new Transition<Integer>(0, 'a', 1));

        m.addTransition(new Transition<Integer>(1, 'b', 2));
        m.addTransition(new Transition<Integer>(1, 'b', 3));
        m.addTransition(new Transition<Integer>(1, 'a', 4));

        m.addTransition(new Transition<Integer>(2, 'a', 0));

        m.addTransition(new Transition<Integer>(3, 'a', 4));

        m.addTransition(new Transition<Integer>(4, 'a', 2));

        new GraphizGenerator<>(m);

        assertTrue(m.accept("abaa"));
    }

    @Test
    public void MachineEpsilonTransition() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(2);

        m.addTransition(new Transition<Integer>(0, Type.EPSILON, 1));
        m.addTransition(new Transition<Integer>(1, Type.EPSILON, 2));

        m.draw();

        assertTrue(m.accept(""));
    }

    @Test
    public void MachineCreatorStartsWith() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = MachineCreator.startsWith("ab", a);

        m.draw();

        assertTrue(m.getLanguageForLength(3).size() == 3);
    }

    @Test
    public void MachineCreatorEndsWith() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = MachineCreator.endsWith("abab", a);

        System.out.println(m.transitions);

        System.out.println(m.getLanguageForLength(5));

        m.draw();

        assertTrue(m.getLanguageForLength(5).size() == 3);
    }

    @Test
    public void MachineCreatorContains() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = MachineCreator.contains("bab", a);

        m.draw();

        assertTrue(m.getLanguageForLength(4).size() == 4);
    }

    @Test
    public void MachineDFAisDFA() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(0);

        m.addTransition(new Transition<Integer>(0, 'a', 0));
        m.addTransition(new Transition<Integer>(0, 'b', 1));

        m.addTransition(new Transition<Integer>(1, 'a', 1));
        m.addTransition(new Transition<Integer>(1, 'b', 0));

        m.draw();

        assertTrue(m.isDFA() == true);
    }

    @Test
    public void MachineNDFAisDFA() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(0);

        m.addTransition(new Transition<Integer>(0, 'a', 0));
        m.addTransition(new Transition<Integer>(0, 'b', 1));

        m.addTransition(new Transition<Integer>(1, 'a', 1));

        m.draw();

        assertTrue(m.isDFA() == false);
    }

    @Test
    public void findEndStatesTest() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(0);

        m.addTransition(new Transition<Integer>(0, Type.EPSILON, 1));

        m.addTransition(new Transition<Integer>(1, 'a', 2));

        m.addTransition(new Transition<Integer>(2, Type.EPSILON, 3));

        m.draw();

        ArrayList<Integer> endstates = m.findEndStates(0, 'a');
        System.out.println(endstates);
    }

    @Test
    public void toDFATest() {
        Character[] a = { 'a', 'b' };
        Machine<Integer> NDFA = new Machine<Integer>(a);
        NDFA.addBeginState(1);
        NDFA.addEndState(5);

        NDFA.addTransition(new Transition<Integer>(1, 'a', 2));
        NDFA.addTransition(new Transition<Integer>(1, 'a', 3));
        NDFA.addTransition(new Transition<Integer>(1, 'b', 4));

        NDFA.addTransition(new Transition<Integer>(2, 'a', 3));
        NDFA.addTransition(new Transition<Integer>(2, 'b', 1));
        NDFA.addTransition(new Transition<Integer>(2, Type.EPSILON, 3));

        NDFA.addTransition(new Transition<Integer>(3, 'a', 3));
        NDFA.addTransition(new Transition<Integer>(3, 'b', 5));
        NDFA.addTransition(new Transition<Integer>(3, Type.EPSILON, 4));

        NDFA.addTransition(new Transition<Integer>(4, 'a', 5));

        NDFA.addTransition(new Transition<Integer>(5, 'a', 4));

        NDFA.draw();

        System.out.println("Epsilon closure for 2: " + NDFA.epsilonClosure(2));

        Machine<String> DFA = NDFA.toDFA();
        DFA.draw();
    }
}
