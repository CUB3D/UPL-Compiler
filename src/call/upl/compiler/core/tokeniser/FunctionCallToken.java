package call.upl.compiler.core.tokeniser;

import call.upl.compiler.core.Pair;

import java.util.List;

/**
 * Created by Callum on 22/09/2015.
 */
public class FunctionCallToken extends ObjectToken<Pair<String, List<ObjectToken>>>
{
    public FunctionCallToken(String functionName, String arguments)
    {
        this.value = new Pair<>(functionName, Tokeniser.tokenise(arguments));
        this.tokenType = Tokeniser.TokenType.FUNCTION_CALL;
    }
}
