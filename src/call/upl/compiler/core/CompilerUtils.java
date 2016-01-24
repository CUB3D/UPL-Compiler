package call.upl.compiler.core;

/**
 * Created by Callum on 06/06/2015.
 */
public class CompilerUtils
{
    public static final String REGEX_MATCH_WORD = "[a-zA-Z@_]";
    public static final String REGEX_MATCH_VARIABLE = "[a-zA-Z0-9_@]+";

    public static final String NUMBER_FORMAT = "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";

    public static final String[] reservedWords = {"if", "while", "return"};

    public static boolean isVariable(String s)
    {
        return s.matches(REGEX_MATCH_VARIABLE);
    }

    public static boolean isNumber(String s)
    {
        return s.matches(NUMBER_FORMAT);
    }

    public static boolean isWord(String s) { return s.matches(REGEX_MATCH_WORD); }

    public static boolean isOperator(String s)
    {
        for(EnumEquationToken operator : EnumEquationToken.values())
        {
            if(operator.isOperator())
            {
                if(operator.getIdentifier().equals(s))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isReservedWord(String s)
    {
        for(String reserved : reservedWords)
        {
            if(s.equals(reserved))
            {
                return true;
            }
        }

        return false;
    }
}
