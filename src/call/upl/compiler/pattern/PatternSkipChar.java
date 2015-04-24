package call.upl.compiler.pattern;

import java.net.PasswordAuthentication;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternSkipChar extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd)
    {
        if(csd.curChar >= csd.text.length)
        {
            return false;
        }

        csd.curChar++;

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "Skp";
    }
}
