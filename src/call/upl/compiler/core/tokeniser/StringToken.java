package call.upl.compiler.core.tokeniser;

/**
 * Created by Callum on 03/09/2015.
 */
public class StringToken extends ObjectToken<String>
{
    public StringToken(String value)
    {
        this.tokenType = Tokeniser.TokenType.STRING;
        this.value = value;
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
