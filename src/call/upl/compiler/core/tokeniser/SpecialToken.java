package call.upl.compiler.core.tokeniser;

/**
 * Created by Callum on 03/09/2015.
 */
public class SpecialToken extends ObjectToken<String>
{
    public SpecialToken(String token)
    {
        this.tokenType = Tokeniser.TokenType.SPECIAL;
        this.value = token;
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
