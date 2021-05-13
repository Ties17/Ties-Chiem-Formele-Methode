import java.util.ArrayList;

class Main {
    public static void main(String[] args) {
        testRegExp();
    }

    public static void testMachine(){
        Character[] a = { 'a', 'b' };

        Machine<String> m = new Machine<String>(a);

        m.addTransition(new Transition<String>("s0", 'b', "s1"));
        m.addTransition(new Transition<String>("s1", 'a', "s2"));
        m.addTransition(new Transition<String>("s2", 'b', "s3"));
        m.addTransition(new Transition<String>("s3", 'a', "s4"));

        System.out.println(m.getLanguageForLength(6));
    }

    public static void testRegExp(){
        RegularExpression re = new RegularExpression("a|b");
        System.out.println(re.accept("a"));
    }
}

