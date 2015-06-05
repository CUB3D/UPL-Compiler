package call.upl.compiler.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 24/04/2015.
 */
public abstract class Pattern
{
    public static final String NUMBER_FORMAT = "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";

    private static List<Pattern> patternList = new ArrayList<Pattern>();

    public static final PatternAnyWord PATTERN_ANY_WORD = new PatternAnyWord();
    public static final PatternSkipChar PATTERN_SKIP_CHAR = new PatternSkipChar();
    public static final PatternAnyWordOrNumber PATTERN_ANY_WORD_OR_NUMBER = new PatternAnyWordOrNumber();
    public static final PatternAnyNumber PATTERN_ANY_NUMBER = new PatternAnyNumber();
    public static final PatternAnySpace PATTERN_ANY_SPACE = new PatternAnySpace();
    public static final PatternExact PATTERN_EXACT = new PatternExact();
    public static final PatternSkipToExact PATTERN_SKIP_TO_EXACT = new PatternSkipToExact();
    public static final PatternAnyVariable PATTERN_ANY_VARIABLE = new PatternAnyVariable();
    public static final PatternSkipToEnd PATTERN_SKIP_TO_END = new PatternSkipToEnd();

    public Pattern()
    {
        Pattern.patternList.add(this);
    }

    public abstract boolean matches(PatternStateData csd, String[] args);
    public abstract String getPatternID();

    public static boolean isNumber(char c)
    {
        return Character.toString(c).matches(NUMBER_FORMAT);
    }

    public static boolean isLetter(char c)
    {
        return Character.toString(c).matches("[a-zA-Z/_@]");
    }


    public static Pattern getPattenFor(String exp)
    {
        for(Pattern p : patternList)
        {
            if(p.getPatternID().equals(exp))
            {
                return p;
            }
        }

        return null;
    }
}
