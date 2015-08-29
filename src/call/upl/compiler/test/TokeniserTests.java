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
     * var -> var
     * Not Implemented:
     * None
     *
     * Return:
     * Implemented:
     * return variable
     * return string
     * return array Element
     * return number
     * Not Implemented:
     * None
     *
     * Include:
     * Implemented:
     * include string
     *
     * While:
     * Implemented:
     * while var == var
     * while var == number
     * while var == string
     * while var == array element
     * while var > number
     * while var > array element
     * while var < number
     * while var < array element
     * while var >= number
     * while var >= array element
     * while var <= number
     * while var <= array element
     * while var != number
     * while var != string
     * while var != array element
     *
     * Not Implemented:
     *
     * while number == var
     * while string == var
     * while array element == var
     *
     * while number > var
     * while array element > var
     *
     * while number < var
     * while array element < var
     *
     * while number >= var
     * while array element >= var
     *
     * while number <= var
     * while array element <= var
     *
     * while number != var
     * while string != var
     * while array element != var
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
    public void testSetVariableVariable()
    {
        String expression = "test = @temp1@";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "test,=,@temp1@";

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

    @Test
    public void testIncludeString()
    {
        String expression = "include \"C:\\abc\\def\\ghi\"";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "include,\",C:\\abc\\def\\ghi,\"";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableEqualsVariable()
    {
        String expression = "while ( test == @temp1@) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,=,=,@temp1@,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableEqualsNumber()
    {
        String expression = "while ( test == 10) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,=,=,10,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableEqualsString()
    {
        String expression = "while ( test == \"This is a test\") ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,=,=,\",This,is,a,test,\",),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableEqualsArrayElement()
    {
        String expression = "while ( test == a[@temp@]) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,=,=,a,[,@temp@,],),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableMoreThanNumber()
    {
        String expression = "while ( test > 10) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,>,10,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableMoreThanArrayElement()
    {
        String expression = "while ( test > a[@temp@]) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,>,a,[,@temp@,],),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableLessThanNumber()
    {
        String expression = "while ( test < 10) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,<,10,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableLessThanArrayElement()
    {
        String expression = "while ( test < a[@temp@]) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,<,a,[,@temp@,],),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableMoreThanEqualNumber()
    {
        String expression = "while ( test >= 10) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,>,=,10,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableMoreThanEqualsArrayElement()
    {
        String expression = "while ( test >= a[@temp@]) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,>,=,a,[,@temp@,],),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableLessThanEqualNumber()
    {
        String expression = "while ( test <= 10) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,<,=,10,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableLessThanEqualArrayElement()
    {
        String expression = "while ( test <= a[@test1@]) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,<,=,a,[,@test1@,],),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableNotEqualsNumber()
    {
        String expression = "while ( test != 10) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,!,=,10,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableNotEqualsString()
    {
        String expression = "while ( test != \"This is a test\") ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,!,=,\",This,is,a,test,\",),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileVariableNotEqualsArrayElement()
    {
        String expression = "while ( test != a[@temp@]) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,!,=,a,[,@temp@,],),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileNumberEqualsVariable()
    {
        String expression = "while ( 10 == test) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,10,=,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileStringEqualsVariable()
    {
        String expression = "while ( \"This is a test\" == test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,\",This,is,a,test,\",=,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileArrayElementEqualsVariable()
    {
        String expression = "while ( a[@temp@] == test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,a,[,@temp@,],=,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileNumberMoreThanVariable()
    {
        String expression = "while ( 10 > test) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,10,>,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileArrayElementMoreThanVariable()
    {
        String expression = "while ( a[@temp@] > test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,a,[,@temp@,],>,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileNumberLessThanVariable()
    {
        String expression = "while ( 10 < test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,10,<,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileArrayElementLessThanVariable()
    {
        String expression = "while ( a[@temp@] < test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,a,[,@temp@,],<,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileNumberMoreThanEqualVariable()
    {
        String expression = "while ( 10 >= test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,10,>,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileArrayElementMoreThanEqualVariable()
    {
        String expression = "while ( test >= a[@temp@]) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,test,>,=,a,[,@temp@,],),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileNumberLessThanEqualsVariable()
    {
        String expression = "while ( 10 <= test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,10,<,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileArrayElementLessThanEqualsVariable()
    {
        String expression = "while ( a[@test@] <= test ) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,a,[,@test@,],<,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileNumberNotEqualsVariable()
    {
        String expression = "while ( 10 != test) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,10,!,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileStringNotEqualsVariable()
    {
        String expression = "while ( \"This is a test\" != test) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,\",This,is,a,test,\",!,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }

    @Test
    public void testWhileArrayElementNotEqualsVariable()
    {
        String expression = "while ( a[@temp@] != test) ->";

        String output = TestUtils.join(Tokeniser.tokenise(expression));

        String expected = "while,(,a,[,@temp@,],!,=,test,),-,>";

        assertEquals(expected, output);

        System.out.println("[" + output + "] [" + expected + "]");
    }
}
