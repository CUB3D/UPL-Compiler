package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternSkipToEnd extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {

        csd.curChar = csd.text.length;

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "SkpEnd";
    }
}
