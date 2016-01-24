package call.upl.compiler.pattern;

import call.upl.compiler.core.tokeniser.Tokeniser;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternBuilder
{
    public static final String PATTERN_TAG_START = "<";
    public static final String PATTERN_TAG_END = ">";
    public static final String PATTERN_TAG_OR = "||";

    private String pattern = "";
    private boolean isInOr;
    private boolean inexactMatch = false;

    public String create(Tokeniser.TokenType type, PatternMatcher.MatchType matchType, String... extra)
    {
        return ("" + type) + " " + ("" + matchType) + ((extra.length == 0) ? "" : " " + extra[0]);
    }

    public void add(Tokeniser.TokenType type, PatternMatcher.MatchType matchType, String... extra)
    {
        if(!isInOr)
        {
            pattern += PATTERN_TAG_START + create(type, matchType, extra) + PATTERN_TAG_END;
        }
        else
        {
            pattern += create(type, matchType, extra) + PATTERN_TAG_OR;
        }
    }

    public void startOr()
    {
        pattern += PATTERN_TAG_START;
        isInOr = true;
    }

    public void endOr()
    {
        pattern = pattern.substring(0, pattern.length() - 2);
        pattern += PATTERN_TAG_END;
        isInOr = false;
    }

    public void enableInexactMatching()
    {
        this.inexactMatch = true;
    }

    public boolean isInexactMatching()
    {
        return inexactMatch;
    }

    @Override
    public String toString()
    {
        return pattern;
    }

    // old below

    public void addMatchAnyWord()
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_ANY_WORD.getPatternID() + PATTERN_TAG_END;
    }

    public void addMatchSpace(int amount)
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_ANY_SPACE.getPatternID() + " " + amount + PATTERN_TAG_END;
    }

    public void addMatchExact(String... s)
    {
        String args = "";

        for(String ss : s)
        {
            args += ss + " ";
        }

        args = args.trim();

        pattern += PATTERN_TAG_START + Pattern.PATTERN_EXACT.getPatternID() + " " + args + PATTERN_TAG_END;
    }

    public void addMatchSkipToExact(String s)
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_SKIP_TO_EXACT.getPatternID() + " " + s + PATTERN_TAG_END;
    }

    public void addMatchVariable()
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_ANY_VARIABLE.getPatternID() + PATTERN_TAG_END;
    }

    public void addSkipToEnd()
    {
        pattern+= PATTERN_TAG_START + Pattern.PATTERN_SKIP_TO_END.getPatternID() + PATTERN_TAG_END;
    }
}
