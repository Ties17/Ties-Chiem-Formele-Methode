package ties;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guru.nidi.graphviz.parse.Parser;
import ties.RegExpression.RegExParser;
import ties.RegExpression.RegExpresion;

public class RegexParserTest {
    
    @Test
    public void testSingleTerminal(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("a");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();

        System.out.println(m.getLanguageForLength(5));

        assertTrue(m.accept("a"));
    }
    
    @Test
    public void testDoubleTerminal(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("ab");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();

        assertTrue(m.accept("ab"));
    }

    @Test
    public void testTerminals(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("abba");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();

        assertTrue(m.accept("ab"));
    }

    @Test
    public void testOrOperator(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("a|b");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();

        assertTrue(m.accept("ab"));
    }

    @Test
    public void testStarOperator(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("a*");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();
    }

    @Test
    public void testPlusOperator(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("a+");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();
    }

    @Test
    public void testBraces1(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("(a|b)|c");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();
    }

    @Test
    public void testBraces2(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("a|(b|c)");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();
    }

    @Test
    public void testBraces3(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("(a|b)|(c|d)");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();
    }

    @Test
    public void testComplex1(){
        RegExParser parser = new RegExParser();

        RegExpresion top = parser.parseString("((cd)+|(a|(bc)))*");

        Machine<Integer> m = top.thompsonConvert();

        m.draw();
    }
}
