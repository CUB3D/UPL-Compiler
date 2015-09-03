package call.upl.compiler.test;

import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import org.junit.Test;

import java.util.List;

/**
 * Created by Callum on 03/09/2015.
 */
public class ObjectTokeniserTests
{
    @Test
    public void testTokenSetVariableNumber()
    {
        String input = "x = 10";

        //x,=,10

        List<ObjectToken> tokens = Tokeniser.convertStringTokenToObjectToken(Tokeniser.tokenise(input));

        for(ObjectToken ot : tokens)
        {
            System.out.println(ot.tokenType + " " + ot.getValue().toString());
        }
    }

    @Test
    public void testTokenSetVariableString()
    {
        String input = "x = \"This is a test\"";

        //x,=,",This,is,a,test,"

        List<ObjectToken> tokens = Tokeniser.convertStringTokenToObjectToken(Tokeniser.tokenise(input));

        for(ObjectToken ot : tokens)
        {
            System.out.println(ot.tokenType + " " + ot.getValue().toString());
        }
    }

    @Test
    public void testTokenSetArrayNumber()
    {
        String input = "a[10] = 11";

        //a,[,10,],=,11

        List<ObjectToken> tokens = Tokeniser.convertStringTokenToObjectToken(Tokeniser.tokenise(input));

        for(ObjectToken ot : tokens)
        {
            System.out.println(ot.tokenType + " " + ot.getValue().toString());
        }
    }
}
