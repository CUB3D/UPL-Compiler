package call.upl.compiler.core.tokeniser;

import call.upl.core.UPLUtils;

/**
 * Created by Callum on 03/09/2015.
 */
public class WordToken extends ObjectToken<String>
{
    public WordToken(String word)
    {
        this.tokenType = Tokeniser.TokenType.WORD;
        this.value = word;
    }

    @Override
    public boolean tagMatches(String[] s)
    {
        if(!super.tagMatches(s))
            return false;

        String mode = s[1];

        if(mode.equals("ANY"))
        {
            return true;
        }
        else
        {
            if(mode.equals("EXACT"))
            {
                return value.equals(s[2]);
            }
        }

        return false;
    }
}
