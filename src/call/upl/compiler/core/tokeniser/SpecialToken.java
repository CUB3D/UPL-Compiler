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
}
