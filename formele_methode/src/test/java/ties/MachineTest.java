package ties;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MachineTest {
    
    @Test
    public void testMachine() {
        Character[] a = { 'a', 'b' };

        Machine<String> m = new Machine<String>(a);
        m.beginState = "0";
        m.endState = "0";

        m.addTransition(new Transition<String>("0", 'a', "0"));
        m.addTransition(new Transition<String>("0", 'b', "1"));

        m.addTransition(new Transition<String>("1", 'a', "1"));
        m.addTransition(new Transition<String>("1", 'b', "0"));

        assertTrue(m.accept(""));
    }
}
