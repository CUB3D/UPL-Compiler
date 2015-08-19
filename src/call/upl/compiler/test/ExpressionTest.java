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

        List<String> expectedAnswer = new ArrayList<>();

        expectedAnswer.add("4.0");
        expectedAnswer.add("6.0");
        expectedAnswer.add("3.0");
        expectedAnswer.add("*");
        expectedAnswer.add("+");

        List<String> answer = null;

        try
        {
            answer = ExpressionParser.convertEquationToRPN(testSimple);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testComplexRPNConversion()
    {
        String testSimple = "((4 + 6) * (3 / 4)) - 2";

        List<String> expectedAnswer = new ArrayList<>();

        expectedAnswer.add("4.0");
        expectedAnswer.add("6.0");
        expectedAnswer.add("+");
        expectedAnswer.add("3.0");
        expectedAnswer.add("4.0");
        expectedAnswer.add("/");
        expectedAnswer.add("*");
        expectedAnswer.add("2.0");
        expectedAnswer.add("-");

        List<String> answer = null;

        try
        {
            answer = ExpressionParser.convertEquationToRPN(testSimple);
        } catch(Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedAnswer, answer);
    }

    @Test
    public void testRPNToUPLBC()
    {
        String input = "(6 + 4) / 2";

        List<String> rpn = null;
        try
        {
            rpn = ExpressionParser.convertEquationToRPN(input);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        List<String> uplbc = ExpressionParser.convertRPNToUPLBC(rpn);

        List<String> expected = new ArrayList<>();

        expected.add("psh 6.0");
        expected.add("psh 4.0");
        expected.add("pop @TEMP1@");
        expected.add("pop @TEMP0@");
        expected.add("add @TEMP0@ @TEMP1@");
        expected.add("psh 2.0");
        expected.add("pop @TEMP1@");
        expected.add("pop @TEMP0@");
        expected.add("div @TEMP0@ @TEMP1@");

        assertEquals(expected, uplbc);
    }
}