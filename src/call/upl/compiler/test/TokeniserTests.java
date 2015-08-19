package call.upl.compiler.test;

import call.upl.compiler.core.ExceptionSystem;
import call.upl.compiler.core.Tokeniser;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Created by Callum on 30/07/2015.
 */
public class TokeniserTests
{
    @Test
    public void testSimpleSet()
    {
        String expression = "test = 10";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,=,10";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testArraySet0()
    {
        String expression = "test[0] = 10";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,[,0,],=,10";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testArraySet1()
    {
        String expression = "test = x[0]";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,=,x,[,0,]";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testArraySet2()
    {
        String expression = "x = [] : 10";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "x,=,[,],:,10";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testStringSet()
    {
        String expression = "test = \"This is a test\"";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,=,\",This,is,a,test,\"";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }
}
