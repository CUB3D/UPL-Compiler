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
}
