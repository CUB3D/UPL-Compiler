package call.upl.compiler.pattern;

import call.upl.compiler.core.CompilerUtils;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternAnyVariable extends Pattern
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

            if(Character.toString(x).matches("[a-zA-Z_@0-9\\[\\]]"))
            {
                csd.curChar++;
                word += x;
                b = true;
            } else
            {
                break;
            }
        }

        if(CompilerUtils.isReservedWord(word))
        {
            return false;
        }

        return b;
    }

    @Override
    public String getPatternID()
    {
        return "Variable";
    }
}
