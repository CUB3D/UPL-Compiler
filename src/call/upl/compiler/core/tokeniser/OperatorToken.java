package call.upl.compiler.core.tokeniser;

/**
 * Created by Callum on 03/09/2015.
 */
public class OperatorToken extends ObjectToken<String>
{
    public OperatorToken(String token)
    {
        this.tokenType = Tokeniser.TokenType.SPECIAL;
        this.value = token;
    }
}
