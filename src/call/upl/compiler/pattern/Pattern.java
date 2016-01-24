package call.upl.compiler.pattern;

import call.upl.compiler.core.CompilerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 24/04/2015.
 */
public abstract class Pattern
{
    private static List<Pattern> patternList = new ArrayList<Pattern>();

    public static final PatternAnyWord PATTERN_ANY_WORD = new PatternAnyWord();
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
