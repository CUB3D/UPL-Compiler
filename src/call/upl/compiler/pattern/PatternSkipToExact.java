package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternSkipToExact extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        String toFind = args[0];

        char[] toFindChars = toFind.toCharArray();

        boolean found = true;
        int foundPos = 0;

        for(int i = csd.curChar; i < csd.text.length; i++)
        {
            found = true;

            for (int i1 = 0; i1 < toFindChars.length; i1++)
            {
                if (csd.text[i + i1] != toFindChars[i1])
                {
                    found = false;
                    break;
                }
            }

            if(found)
            {
                foundPos = i;
                break;
            }
        }

        if(!found)
            return false;

        csd.curChar = foundPos + toFind.length();

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "SkpExt";
    }
}
