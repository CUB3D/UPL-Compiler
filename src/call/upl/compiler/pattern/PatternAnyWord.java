package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternAnyWord extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd)
    {
        if(csd.curChar >= csd.text.length)
        {
            return false;
        }

        boolean b = false;

        while(true)
        {
            if(csd.curChar >= csd.text.length)
            {
                break;
            }

            char x = csd.text[csd.curChar];

            if(isLetter(x))
            {
                csd.curChar++;
                b = true;
            } else
            {
                break;
            }
        }

        if(!b)
            return false;

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "Wrd";
    }
}
