package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternExact extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        String toFind = args[0];
        char[] toFindChars = toFind.toCharArray();

        for(int i = 0; i < toFind.length(); i++)
        {
            if(csd.curChar + i >= csd.text.length)
            {
                return false;
            }

            if(csd.text[csd.curChar + i] != toFindChars[i])
            {
                return false;
            }
        }

        csd.curChar += toFind.length();

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "Mat";
    }
}
