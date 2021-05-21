package ties;

class App {
    public static void main(String[] args) {
        testMachine();
    }

    public static void testMachine() {
        Character[] a = { 'a', 'b' };

        Machine<String> m = new Machine<String>(a);
        m.beginState = "0";
        m.endState = "0";

        m.addTransition(new Transition<String>("0", 'a', "0"));
        m.addTransition(new Transition<String>("0", 'b', "1"));

        m.addTransition(new Transition<String>("1", 'a', "1"));
        m.addTransition(new Transition<String>("1", 'b', "0"));

        System.out.println(m.accept("abbbab"));
    }

    public static void testRegExp() {
        RegularExpression re = new RegularExpression("a|b");
        System.out.println(re.accept("a"));
    }
}
