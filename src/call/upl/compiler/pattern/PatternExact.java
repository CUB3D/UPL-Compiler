package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternExact extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        for(String arg : args)
        {
            String toFind = arg;
            char[] toFindChars = toFind.toCharArray();
            boolean success = true;

            for(int i = 0; i < toFind.length(); i++)
            {
                if(csd.curChar + i >= csd.text.length)
                {
                    success = false;
                }
                else
                {
                    if(csd.text[csd.curChar + i] != toFindChars[i])
                    {
                        success = false;
                    }
                }
            }

            if(success)
            {
                csd.curChar += toFind.length();

                return true;
            }
        }

        return false;
    }

    @Override
    public String getPatternID()
    {
        return "Mat";
    }
}
