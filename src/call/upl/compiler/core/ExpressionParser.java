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

        System.out.println("Parsing: " + s);
        System.out.println("ReturnValue: " + result);

        s = s.replaceAll(result, "");
        s = s.replaceAll(" ", "");

        if(s.startsWith("=")) s = s.substring(1);

        System.out.println("Equation: " + s);

        List<String> returnValue = new ArrayList<String>();

        breakdownExpressionImpl(s, returnValue);

        for(String s1 : returnValue)
        {
            System.out.println("ReturnValue: " + s1);
        }

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

                System.out.println("SubExpr: " + subExp);

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
}
