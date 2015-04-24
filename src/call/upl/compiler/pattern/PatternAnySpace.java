package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternAnySpace extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd)
    {
        if(csd.curChar >= csd.text.length)
        {
            return false;
        }

        while(true)
        {
            if(csd.curChar >= csd.text.length)
            {
                break;
            }

            char x = csd.text[csd.curChar];

            if(x == ' ')
            {
                csd.curChar++;
            } else
            {
                break;
            }
        }

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "Spce";
    }
}
