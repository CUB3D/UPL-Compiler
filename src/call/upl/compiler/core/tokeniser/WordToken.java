package call.upl.compiler.core.tokeniser;

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
}
