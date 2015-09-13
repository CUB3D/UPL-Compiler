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

        return false;
    }

    @Override
    public String toCodeValue()
    {
        //value,[,index,]
        return value.first + "[" + value.second + "]";
    }
}
