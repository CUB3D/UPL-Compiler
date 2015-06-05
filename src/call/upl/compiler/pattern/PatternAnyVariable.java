package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternAnyVariable extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        boolean b = false;

        String s = "";

        while(true)
        {
            if(csd.curChar >= csd.text.length)
            {
                break;
            }

            char x = csd.text[csd.curChar];

            if(Character.toString(x).matches("[a-zA-Z_@0-9\\[\\]]"))
            {
                csd.curChar++;
                s += x;
                b = true;
            } else
            {
                break;
            }
        }

        return b;
    }

    @Override
    public String getPatternID()
    {
        return "Variable";
    }
}
