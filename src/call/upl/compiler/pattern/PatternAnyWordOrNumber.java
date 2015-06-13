package call.upl.compiler.pattern;

import call.upl.compiler.core.CompilerUtils;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternAnyWordOrNumber extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        boolean b = false;

        String word = "";

        while(true)
        {
            if(csd.curChar >= csd.text.length)
            {
                break;
            }

            char x = csd.text[csd.curChar];

            if(CompilerUtils.isNumber("" + x) || CompilerUtils.isWord("" + x))
            {
                word += x;
                csd.curChar++;
                b = true;
            }
            else
            {
                break;
            }
        }

        if(!b)
        {
            return false;
        }

        if(CompilerUtils.isReservedWord(word))
        {
            return false;
        }

        return true;
    }

    @Override
    public String getPatternID()
    {
        return "Wrd|Num";
    }
}
