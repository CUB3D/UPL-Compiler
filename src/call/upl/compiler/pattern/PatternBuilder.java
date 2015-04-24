package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternBuilder
{
    public static final String PATTERN_TAG_START = "<";
    public static final String PATTERN_TAG_END = ">";

    private String pattern = "";

    public void addMatchAnyWord()
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_ANY_WORD.getPatternID() + PATTERN_TAG_END;
    }

    public void addMatchSpace(int amount)
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_ANY_SPACE.getPatternID() + " " + amount + PATTERN_TAG_END;
    }

    public void addMatchExact(String s)
    {
        pattern += PATTERN_TAG_START + "mat " + s + ">";
    }

    public void addMatchValue()
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_ANY_WORD_OR_NUMBER.getPatternID() + PATTERN_TAG_END;
    }

    public void addMatchSkipChar()
    {
        pattern += PATTERN_TAG_START + Pattern.PATTERN_SKIP_CHAR.getPatternID() + PATTERN_TAG_END;
    }

    @Override
    public String toString()
    {
        return pattern;
    }
}
