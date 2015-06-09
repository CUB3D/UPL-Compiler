package call.upl.compiler.test;

import call.upl.compiler.core.ExpressionParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Callum on 09/06/2015.
 */
public class ExpressionTest
{
    @Test
    public void testTokenise()
    {
        String testSimple = "4 + 6 * 3";

        List<String> answer = ExpressionParser.tokenise(testSimple);

        List<String> expectedAnswer = new ArrayList<>();

        expectedAnswer.add("4.0");
        expectedAnswer.add("+");
        expectedAnswer.add("6.0");
        expectedAnswer.add("*");
        expectedAnswer.add("3.0");

        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testComplexTokenise()
    {
        String testSimple = "((4 + 6) * (3 / 4)) - 2";

        List<String> answer = ExpressionParser.tokenise(testSimple);

        List<String> expectedAnswer = new ArrayList<>();

        expectedAnswer.add("(");
        expectedAnswer.add("(");
        expectedAnswer.add("4.0");
        expectedAnswer.add("+");
        expectedAnswer.add("6.0");
        expectedAnswer.add(")");
        expectedAnswer.add("*");
        expectedAnswer.add("(");
        expectedAnswer.add("3.0");
        expectedAnswer.add("/");
        expectedAnswer.add("4.0");
        expectedAnswer.add(")");
        expectedAnswer.add(")");
        expectedAnswer.add("-");
        expectedAnswer.add("2.0");

        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testRPNConversion()
    {
        String testSimple = "4 + 6 * 3";

        String expectedAnswer = "4.06.03.0*+";

        String answer = null;

        try
        {
            answer = ExpressionParser.convertEquationToRPN(testSimple);
        } catch(Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testComplexRPNConversion()
    {
        String testSimple = "((4 + 6) * (3 / 4)) - 2";

        String expectedAnswer = "4.06.0+3.04.0/*2.0-";

        String answer = null;

        try
        {
            answer = ExpressionParser.convertEquationToRPN(testSimple);
        } catch(Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedAnswer, answer);
    }
}