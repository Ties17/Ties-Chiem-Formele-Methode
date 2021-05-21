package ties;

class App {
    public static void main(String[] args) {
        testMachine();
    }

    public static void testMachine() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(0);
        
        m.addTransition(new Transition<Integer>(0, 'a', 0));
        m.addTransition(new Transition<Integer>(0, 'b', 1));

        m.addTransition(new Transition<Integer>(1, 'a', 1));
        m.addTransition(new Transition<Integer>(1, 'b', 0));

        System.out.println(m.getLanguageForLength(5));
    }

    public static void testRegExp() {
        RegularExpression re = new RegularExpression("a|b");
        System.out.println(re.accept("a"));
    }
}
