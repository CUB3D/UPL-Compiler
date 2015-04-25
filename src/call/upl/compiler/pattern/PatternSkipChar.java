package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternSkipChar extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        csd.curChar++;

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "Skp";
    }
}
