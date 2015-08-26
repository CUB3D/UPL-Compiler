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
    /**
     * Set:
     * Implemented:
     * var -> number
     * array-> number
     * var -> array
     * array creation
     * var -> string
     * array -> array
     * array -> string
     * Not Implemented:
     * var -> var
     *
     * Return:
     * Implemented:
     * return variable
     * return string
     * return array Element
     * return number
     *
     * Not Implemented:
     *
     **/


    @Test
    public void testSetVariableNumber()
    {
        String expression = "test = 10";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,=,10";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testSetArrayNumber()
    {
        String expression = "test[0] = 10";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,[,0,],=,10";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testSetVariableArray()
    {
        String expression = "test = x[0]";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,=,x,[,0,]";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testCreateArray()
    {
        String expression = "x = [] : 10";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "x,=,[,],:,10";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testSetVariableString()
    {
        String expression = "test = \"This is a test\"";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,=,\",This,is,a,test,\"";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testSetArrayArray()
    {
        String expression = "@test1@[@temp1@] = @temp1@[@test1@]";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "@test1@,[,@temp1@,],=,@temp1@,[,@test1@,]";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testSetArrayString()
    {
        String expression = "@test1@[@temp1@] = \"This is a test\"";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "@test1@,[,@temp1@,],=,\",This,is,a,test,\"";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testReturnVariable()
    {
        String expression = "return @temp1@";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "return,@temp1@";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testReturnString()
    {
        String expression = "return \"This is a test\"";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "return,\",This,is,a,test,\"";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testReturnArrayElement()
    {
        String expression = "return a[10]";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "return,a,[,10,]";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testReturnNumber()
    {
        String expression = "return 10";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "return,10";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }
}
