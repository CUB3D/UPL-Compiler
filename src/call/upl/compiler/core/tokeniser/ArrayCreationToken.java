package call.upl.compiler.core.tokeniser;

import call.upl.compiler.core.Pair;

import java.util.List;

/**
 * Created by Callum on 05/09/2015.
 */
public class ArrayCreationToken extends ObjectToken<Pair<List<String>, String>>
{
    public ArrayCreationToken(List<String> contents, String size)
    {
        this.value = new Pair<>(contents, size);

        this.tokenType = Tokeniser.TokenType.ARRAY_CREATION;
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
}
