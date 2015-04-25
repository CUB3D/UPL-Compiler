package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternAnySpace extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        int start = csd.curChar;

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

        String amount = args[0];
        int amountInt = Integer.parseInt(amount);

        if(csd.curChar - start < amountInt)
        {
            return false;
        }

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "Spce";
    }
}
