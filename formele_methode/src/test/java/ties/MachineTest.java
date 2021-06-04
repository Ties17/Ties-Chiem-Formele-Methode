package ties;

import static org.junit.Assert.assertTrue;

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

        new GraphizGenerator<>(m);
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
    public void MachineNDFA(){
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
    public void MachineEpsilonTransition(){
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(2);

        m.addTransition(new Transition<Integer>(0, null, 1));
        m.addTransition(new Transition<Integer>(1, null, 2));

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

        assertTrue(m.getLanguageForLength(5).size() == 3);
    }

    @Test
    public void MachineCreatorContains() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = MachineCreator.contains("bab", a);

        m.draw();

        assertTrue(m.getLanguageForLength(4).size() == 4);
    }
}
