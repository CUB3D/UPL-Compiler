package call.upl.compiler.core;

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

    public static String convertEquationToRPN(String equation) throws Exception
    {
        List<String> tokens = tokenise(equation);

        String reversePolishNotation = "";

        Stack<EnumEquationToken> operatorStack = new Stack<>();

        for(String token : tokens)
        {
            if(CompilerUtils.isNumber(token) || CompilerUtils.isVariable(token))
            {
                reversePolishNotation += token;
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
                            reversePolishNotation += o2.getIdentifier();
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
                    reversePolishNotation += popedToken.getIdentifier();

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

            reversePolishNotation += popedToken.getIdentifier();
        }

        return reversePolishNotation;
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

        for(String s : subExpressions) System.out.println(s);

        convertExpressionToCodeImpl(subExpressions, compiler);
    }

    public static void sortTokens(List<String> tokens)
    {
        //NOOP
    }

    public static int valueCount = 0;
    public static EnumEquationToken curOperand;

    public static void parseToken(UPLCompiler compiler, String token)
    {
        //if is number
        if(token.matches(CompilerUtils.NUMBER_FORMAT))
        {
            compiler.writeCode("psh " + token);

            valueCount++;
        }
        else
        {
            curOperand = getOperator(token);
        }

        if(valueCount == 2)
        {
            valueCount = 1;

            compiler.writeCode("pop @TEMP0@");
            compiler.writeCode("pop @TEMP1@");

            compiler.writeCode(curOperand.getOpcode() + " @TEMP0@ @TEMP1@");
        }
    }

    public static void convertExpressionToCodeImpl(List<String> subExpressions, UPLCompiler uplCompiler)
    {
        String result = subExpressions.get(0);

        for(int i = 1; i < subExpressions.size(); i++)
        {
            String s = subExpressions.get(i);

            List<String> tokens = tokenise(s);

            sortTokens(tokens);

            if(UPLCompiler.DEBUG)
            {
                for(String ss : tokens)
                {
                    System.out.println(ss);
                }
            }

            for(String token : tokens)
            {
                parseToken(uplCompiler, token);
            }

            if(true) continue; // skip old code

            if(s.length() >= 3)
            {
                EnumEquationToken operand = getOperator(s);

                String[] args = s.split(operand.getIdentifierEscaped());

                uplCompiler.writeCode(operand.getOpcode() + " " + args[0] + " " + args[1]);
            } else
            {
                // if in form -+*/ x then use one temp
                // if in form -+*/ then use two

                if(s.length() == 1)
                {
                    EnumEquationToken operand = getOperator(s);

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

                    EnumEquationToken operand = getOperator(s);

                    String left;
                    String right;

                    if(isLeftEmpty)
                    {
                        left = "@TEMP0@";

                        s = s.replace(operand.getIdentifier(), "");
                        right = s;
                    } else
                    {
                        right = "@TEMP0@";

                        s = s.replace(operand.getIdentifier(), "");
                        left = s;
                    }

                    uplCompiler.writeCode("pop @TEMP0@");

                    uplCompiler.writeCode(operand.getOpcode() + " " + left + " " + right);
                }
            }

            uplCompiler.writeCode("pop " + result);
        }
    }
}
