package ties.RegExpression;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import ties.RegExpression.Operators.Operator;

public class RegExpresion {
    
    Operator operator;
    String terminals;
    
    RegExpresion left;
    RegExpresion right;
            
    public RegExpresion()
    {
        operator = Operator.ONE;
        terminals = "";
        left = null;
        right = null;
    }
    
    public RegExpresion(String p)
    {
        operator = Operator.ONE;
        terminals = p;
        left = null;
        right = null;
    }
    
    public RegExpresion plus()
    {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.PLUS;
        result.left = this;
        return result;
    }

    public RegExpresion star()
    {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.STAR;
        result.left = this;
        return result;
    }

    public RegExpresion or(RegExpresion e2)
    {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.OR;
        result.left = this;
        result.right = e2;
        return result;
    }

    public RegExpresion dot(RegExpresion e2)
    {
        RegExpresion result = new RegExpresion();
        result.operator = Operator.DOT;
        result.left = this;
        result.right = e2;
        return result;
    }

    public SortedSet <String>  getLanguage(int maxSteps)
    {
        RegexCompareByLength regexCompareByLength = new RegexCompareByLength();
        SortedSet<String> emptyLanguage = new TreeSet<String>(regexCompareByLength.compareByLength);
        SortedSet<String> languageResult = new TreeSet<String>(regexCompareByLength.compareByLength);
        
        SortedSet<String> languageLeft, languageRight;
        
        if (maxSteps < 1) return emptyLanguage;
        
        switch (this.operator) {
            case ONE:
                 languageResult.add(terminals);

            case OR:
                languageLeft = left == null ? emptyLanguage : left.getLanguage(maxSteps - 1);
                languageRight = right == null ? emptyLanguage : right.getLanguage(maxSteps - 1);
                languageResult.addAll (languageLeft);
                languageResult.addAll (languageRight);
                break;
                

            case DOT:
                languageLeft = left == null ? emptyLanguage : left.getLanguage(maxSteps - 1);
                languageRight = right == null ? emptyLanguage : right.getLanguage(maxSteps - 1);
                for (String s1 : languageLeft)
                    for (String s2 : languageRight)
                       {languageResult.add (s1 + s2);}
                break;

            // STAR(*) en PLUS(+) kunnen we bijna op dezelfde manier uitwerken:
            case STAR:
            case PLUS:
                languageLeft = left == null ? emptyLanguage : left.getLanguage(maxSteps - 1);
                languageResult.addAll(languageLeft);
                for (int i = 1; i < maxSteps; i++)
                {   
                    HashSet<String> languageTemp = new HashSet<String>(languageResult);
                    for (String s1 : languageLeft)
                    {   for (String s2 : languageTemp)
                        {   languageResult.add (s1+s2);
                        }
                    }
                }
                if (this.operator  == Operator.STAR)
                    {languageResult.add("");}
                break;

                
            default:
                System.out.println ("getLanguage is nog niet gedefinieerd voor de operator: " + this.operator);
                break;
        }        
                
            
        return languageResult;
    }  
}

