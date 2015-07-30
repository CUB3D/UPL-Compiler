package call.upl.compiler.core;

import call.upl.core.UPL;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Callum on 26/05/2015.
 */
public class ExpressionParser
{
    public static List<String> tokenise(String s)
    {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(s));

        List<String> tokens = new ArrayList<String>();

        tokenizer.ordinaryChar('/');

        try
        {
            while(tokenizer.nextToken() != StreamTokenizer.TT_EOF)
            {
                switch(tokenizer.ttype)
                {
                    case StreamTokenizer.TT_NUMBER:
                        tokens.add("" + tokenizer.nval);
                        break;
                    case StreamTokenizer.TT_WORD:
                        tokens.add(tokenizer.sval);
                        break;
                    default:
                        tokens.add("" + (char) tokenizer.ttype);
                        break;
                }
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        return tokens;
    }

    public static List<String> convertEquationToRPN(String equation) throws Exception
    {
        List<String> tokens = tokenise(equation);

        List<String> returnQueue = new ArrayList<>();

        Stack<EnumEquationToken> operatorStack = new Stack<>();

        for(String token : tokens)
        {
            if(CompilerUtils.isNumber(token) || CompilerUtils.isVariable(token))
            {
                returnQueue.add(token);
            }

            if(CompilerUtils.isOperator(token))
            {
                EnumEquationToken o1 = getOperator(token);

                if(operatorStack.size() > 0 && operatorStack.peek().isOperator())
                {
                    while(operatorStack.size() > 0)
                    {
                        EnumEquationToken o2 = operatorStack.peek();

                        if((o1.isLeftAssociative() && o1.getPrecedence() <= o2.getPrecedence()) || (!o1.isLeftAssociative() && o1.getPrecedence() < o2.getPrecedence()))
                        {
                            operatorStack.pop();
                            returnQueue.add(o2.getIdentifier());
                        } else
                        {
                            break;
                        }
                    }
                }

                operatorStack.push(o1);
            }

            if(token.equals("("))
            {
                operatorStack.push(EnumEquationToken.OPENPAREN);
            }

            if(token.equals(")"))
            {
                EnumEquationToken popedToken = operatorStack.pop();

                while(popedToken != EnumEquationToken.OPENPAREN)
                {
                    returnQueue.add(popedToken.getIdentifier());

                    if(operatorStack.size() == 0)
                    {
                        ExceptionSystem.throwException("Error in RPN conversion, Mismatched parenthesis");
                    }

                    popedToken = operatorStack.pop();
                }
            }
        }

        while(operatorStack.size() > 0)
        {
            EnumEquationToken popedToken = operatorStack.pop();

            if(popedToken == EnumEquationToken.OPENPAREN || popedToken == EnumEquationToken.CLOSEPAREN)
            {
                ExceptionSystem.throwException("Error in RPN conversion, Mismatched parenthesis");
            }

            returnQueue.add(popedToken.getIdentifier());
        }

        return returnQueue;
    }

    public static EnumEquationToken getOperator(String s)
    {
        for(EnumEquationToken operand : EnumEquationToken.values())
        {
            if(operand.isOperator())
            {
                if(operand.getIdentifier().equals(s))
                {
                    return operand;
                }
            }
        }

        return null;
    }

    public static List<String> convertRPNToUPLBC(List<String> rpn)
    {
        List<String> result = new ArrayList<>();

        for(String token : rpn)
        {
            if(CompilerUtils.isVariable(token) || CompilerUtils.isNumber(token))
            {
                result.add("psh " + token);
            }

            if(CompilerUtils.isOperator(token))
            {
                int argCount = 2;

                EnumEquationToken operator = getOperator(token);

                String s = operator.getOpcode();

                for(int i = 0; i < argCount; i++)
                {
                    result.add("pop @TEMP" + (argCount - i - 1) + "@");

                    s += " @TEMP" + i + "@";
                }

                result.add(s);
            }
        }

        return result;
    }

    public static void convertEquationToCode(String equation, UPLCompiler compiler)
    {
        String[] segments = equation.split("=");

        String returnValue = segments[0].trim();
        equation = segments[1].trim();

        List<String> RPN = null;

        try
        {
            RPN = convertEquationToRPN(equation);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        List<String> UPLBC = convertRPNToUPLBC(RPN);

        for(String line : UPLBC)
        {
            compiler.writeCode(line);
        }

        compiler.writeCode("pop " + returnValue);
    }
}
