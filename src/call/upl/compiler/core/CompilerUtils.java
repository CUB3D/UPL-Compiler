package call.upl.compiler.core;

/**
 * Created by Callum on 06/06/2015.
 */
public class CompilerUtils
{
    public static final String REGEX_MATCH_VARIABLE = "[a-zA-Z0-9/_@]+";
    public static final String REGEX_MATCH_ARRAY_ACCESS = REGEX_MATCH_VARIABLE + "\\s?\\[\\s?" + REGEX_MATCH_VARIABLE + "\\s?\\]";

    public static final String REGEX_MATCH_STRING = "\"[\\w\\d\\s]+\"";

    public static boolean isVariable(String s)
    {
        return s.matches(REGEX_MATCH_VARIABLE);
    }

    public static boolean isArrayAccess(String s)
    {
        return s.matches(REGEX_MATCH_ARRAY_ACCESS);
    }

    public static boolean isString(String s)
    {
        return s.matches(REGEX_MATCH_STRING);
    }
}
