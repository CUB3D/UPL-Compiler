package call.upl.compiler.core.tokeniser;

import call.upl.compiler.core.Pair;
import call.upl.core.value.ArrayValue;

/**
 * Created by Callum on 03/09/2015.
 */
public class ArrayAccessToken extends ObjectToken<Pair<String, String>>
{
    public ArrayAccessToken(String variable, String index)
    {
        this.value = new Pair<>(variable, index);
        // index is stored as String because the value of the index doesn't matter, so this saves detecting if is is a variable or a number

        this.tokenType = Tokeniser.TokenType.ARRAY_ACCESS;
    }
}
