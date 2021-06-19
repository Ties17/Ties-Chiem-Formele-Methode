package ties;

import ties.RegExpression.RegExpresion;
import ties.UI.MainScreen;

class App {
    public static void main(String[] args) {
        new MainScreen();
    }

    public static void testMachine() {
        Character[] a = { 'a', 'b' };

        Machine<Integer> m = new Machine<Integer>(a);
        m.addBeginState(0);
        m.addEndState(2);

        m.addTransition(new Transition<Integer>(0, 'a', 1));
        m.addTransition(new Transition<Integer>(0, 'b', -1));

        m.addTransition(new Transition<Integer>(1, 'b', 1));
        m.addTransition(new Transition<Integer>(1, 'a', 2));

        m.addTransition(new Transition<Integer>(2, 'b', -1));
        m.addTransition(new Transition<Integer>(2, 'a', -1));

        System.out.println(m.getLanguageForLength(5));

    }

    public static void testRegExp() {
        RegExpresion expr1, expr2, expr3, expr4, expr5, a, b, all;
        a = new RegExpresion("a");
        b = new RegExpresion("b");

        // expr1: "baa"
        expr1 = new RegExpresion("baa");

        // expr2: "bb"
        expr2 = new RegExpresion("bb");
        // expr3: "baa | baa"
        expr3 = expr1.or(expr2);

        // all: "(a|b)*"
        all = (a.or(b)).star();

        // expr4: "(baa | baa)+"
        expr4 = expr3.plus();
        // expr5: "(baa | baa)+ (a|b)*"
        expr5 = expr4.dot(all);

        System.out.println("taal van (baa):\n" + expr1.getLanguage(5));
        System.out.println("taal van (bb):\n" + expr2.getLanguage(5));
        System.out.println("taal van (baa | bb):\n" + expr3.getLanguage(5));

        System.out.println("taal van (a|b)*:\n" + all.getLanguage(5));
        System.out.println("taal van (baa | bb)+:\n" + expr4.getLanguage(5));
        System.out.println("taal van (baa | bb)+ (a|b)*:\n" + expr5.getLanguage(6));
    }
}
