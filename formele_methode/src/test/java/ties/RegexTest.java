package ties;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import ties.RegExpression.RegExpresion;
import ties.RegExpression.RegexCompareByLength;

public class RegexTest {
    
    @Test
    public void testLanguage1()
    {
        RegExpresion expr1 = new RegExpresion("baa");
        RegExpresion expr2 = new RegExpresion("bb");

        RegexCompareByLength regexCompareByLength = new RegexCompareByLength();
        SortedSet<String> languageResult = new TreeSet<String>(regexCompareByLength.compareByLength);

        languageResult.add("baa");
        Assert.assertEquals(expr1.getLanguage(5), languageResult);

        languageResult = new TreeSet<String>(regexCompareByLength.compareByLength);
        languageResult.add("bb");
        Assert.assertEquals(expr2.getLanguage(5), languageResult);
    }

    @Test
    public void testLanguageOr() {
        RegExpresion expr1 = new RegExpresion("baa");
        RegExpresion expr2 = new RegExpresion("bb");
        RegExpresion expr3 = expr1.or(expr2);

        RegexCompareByLength regexCompareByLength = new RegexCompareByLength();
        SortedSet<String> languageResult = new TreeSet<String>(regexCompareByLength.compareByLength);

        languageResult.add("bb");
        languageResult.add("baa");
        Assert.assertEquals(expr3.getLanguage(5), languageResult);
    }

    @Test
    public void testLanguageAll() {
            //     // all: "(a|b)*"
    //     all = (a.or(b)).star();
        
        RegExpresion expr1 = new RegExpresion("a");
        RegExpresion expr2 = new RegExpresion("b");
        RegExpresion all;

        all = (expr1.or(expr2)).star();

        RegexCompareByLength regexCompareByLength = new RegexCompareByLength();
        SortedSet<String> languageResult = new TreeSet<String>(regexCompareByLength.compareByLength);

        languageResult.add("");
        languageResult.add("a");
        languageResult.add("b");
        languageResult.add("aa");
        languageResult.add("ab");
        languageResult.add("ba");
        languageResult.add("bb");
        languageResult.add("aaa");
        languageResult.add("aab");
        languageResult.add("aba");
        languageResult.add("abb");
        languageResult.add("baa");
        languageResult.add("bab");
        languageResult.add("bba");
        languageResult.add("bbb");

        Assert.assertEquals(all.getLanguage(3), languageResult);
    }

    @Test
    public void testThompsonConversion(){
        // az((a | z | aa) z)* (aaa|zz)+
        RegExpresion a = new RegExpresion("a");
        RegExpresion z = new RegExpresion("z");

        RegExpresion dot1 = a.dot(z);
        RegExpresion or1 = (a.or(z)).or(a.dot(a));
        RegExpresion star1 = or1.dot(z).star();
        RegExpresion plus1 = (((a.dot(a)).dot(a)).or(z.dot(z))).plus();
        RegExpresion all = (dot1.dot(star1)).dot(plus1);

        Machine<Integer> m = all.thompsonConvert();

        m.draw();
    }

    @Test
    public void testEquals(){
        RegExpresion expr1 = new RegExpresion("a");
        RegExpresion expr2 = new RegExpresion("b");
        RegExpresion all;
        RegExpresion all2;

        all = (expr1.or(expr2)).star();
        all2 = (expr1.or(expr2)).star();

        System.out.println(all.isEqualTo(all2));
    }
}
