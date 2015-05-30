package call.upl.compiler.core;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 26/05/2015.
 */
public class ExpressionParser
{
    public static List<String> breakdownExpression(String s)
    {
        //get variable for result
        String result = getVarForResult(s);

        List<String> returnValue = new ArrayList<String>();

        returnValue.add(result);

        result = result.replaceAll("\\[", "\\\\[");
        result = result.replaceAll("\\]", "\\\\]");

        s = s.replaceFirst(result, "");
        s = s.replace(" ", "");

        if(s.startsWith("=")) s = s.substring(1);

        breakdownExpressionImpl(s, returnValue);

        return returnValue;
    }

    public static void breakdownExpressionImpl(String exp, List<String> returnValue)
    {
        char[] characters = exp.toCharArray();

        String expValue = "";

        for(int i = 0; i < exp.length(); i++)
        {
            char character = characters[i];

            if(character == '(')
            {
                String subExp = "";

                i++;


                int opens = 0;

                while(true)
                {
                    character = characters[i];

                    if(character == '(')
                    {
                        opens++;
                    }

                    if(character == ')')
                    {
                        opens--;

                        if(opens < 0)
                        {
                            break;
                        }
                    }

                    subExp += character;

                    i++;
                }

                breakdownExpressionImpl(subExp, returnValue);

                continue;
            }

            expValue += character;
        }

        returnValue.add(expValue);
    }

    public static String getVarForResult(String s)
    {
        String[] args = s.split("=");
        String returnValue = args[0];
        returnValue = returnValue.replaceAll(" ", "");

        return returnValue;
    }

    public static void convertExpressionToCode(String exp, UPLCompiler compiler)
    {
        List<String> subExpressions = breakdownExpression(exp);

        convertExpressionToCodeImpl(subExpressions, compiler);
    }

    public static void convertExpressionToCodeImpl(List<String> subExpressions, UPLCompiler uplCompiler)
    {
        String result = subExpressions.get(0);

        for(int i = 1; i < subExpressions.size(); i++)
        {
            String s = subExpressions.get(i);

            // should be in form x+y
            if(s.length() >= 3)
            {
                EnumOperand operand = getOperand(s);

                String[] args = s.split(operand.getIdentifierEscaped());

                uplCompiler.writeCode(operand.getOpcode() + " " + args[0] + " " + args[1]);
            }
            else
            {
                // if in form -+*/ x then use one temp
                // if in form -+*/ then use two

                if(s.length() == 1)
                {
                    EnumOperand operand = getOperand(s);

                    uplCompiler.writeCode("pop @TEMP0@");
                    uplCompiler.writeCode("pop @TEMP1@");

                    uplCompiler.writeCode(operand.getOpcode() + " @TEMP0@ @TEMP1@");
                }

                if(s.length() >= 2)
                {
                    boolean isLeftEmpty = false;

                    char first = s.charAt(0);

                    if(first == '+' || first == '-' || first == '/' || first == '*' || first == '%')
                    {
                        isLeftEmpty = true;
                    }

                    EnumOperand operand = getOperand(s);

                    String left;
                    String right;

                    if(isLeftEmpty)
                    {
                        left = "@TEMP0@";

                        s = s.replace(operand.getIdentifier(), "");
                        right = s;
                    }
                    else
                    {
                        right = "@TEMP0@";

                        s = s.replace(operand.getIdentifier(), "");
                        left = s;
                    }

                    uplCompiler.writeCode("pop @TEMP0@");

                    uplCompiler.writeCode(operand.getOpcode() + " " + left + " " + right);
                }
            }
        }

        uplCompiler.writeCode("pop " + result);
    }

    public static EnumOperand getOperand(String s)
    {
        EnumOperand operand = null;

        for(EnumOperand operand1 : EnumOperand.values())
        {
            if(s.contains(operand1.getIdentifier()))
            {
                operand = operand1;
            }
        }

        return operand;
    }
}