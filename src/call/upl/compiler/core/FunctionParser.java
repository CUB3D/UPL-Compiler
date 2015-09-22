package call.upl.compiler.core;

import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;

import java.util.List;

/**
 * Created by Callum on 05/06/2015.
 */
public class FunctionParser
{
    public static void convertFunctionToCode(UPLCompiler compiler, List<ObjectToken> function)
    {
        for (ObjectToken t : function)
        {
            System.out.println(t.tokenType + " " + t.toString());
        }
        //foo = bar("foobar", 20)

        String returnValue = function.get(0).toCodeValue(); // could be a WORD or ARRAY_ACCESS type

        String functionName = function.get(2).toCodeValue(); // will always be a WORD type

        String argumentString = "";

        for(int i = 4; i < function.size() - 1; i++) // could change to i += 2 to remove if
        {
            ObjectToken token = function.get(i);

            if(token.tokenType == Tokeniser.TokenType.SPECIAL)
            {
                if(token.toCodeValue().equals(","))
                {
                    convertArgumentToCode(compiler, argumentString);

                    argumentString = "";

                    continue;
                }
            }

            argumentString += token.toString();
        }

        if(!argumentString.isEmpty())
        {
            convertArgumentToCode(compiler, argumentString);
        }

        compiler.writeCode("jmp " + functionName);

        if(returnValue != null)
        {
            compiler.writeCode("pop " + returnValue);
        }
    }

    public static String rebuildFunction(List<ObjectToken> tokens)
    {
        String s = "";

        int braceCounter = 0;

        for(int i = 0; i < tokens.size(); i++)
        {
            s += tokens.get(i).toCodeValue();

            if(tokens.get(i).tagMatches(new String[]{"SPECIAL", "EXACT", "("}))
            {
                braceCounter++;
            }
            if(tokens.get(i).tagMatches(new String[]{"SPECIAL", "EXACT", ")"}))
            {
                if(braceCounter > 0)
                {
                    braceCounter--;

                    if(braceCounter == 0)
                    {
                        break;
                    }
                }
            }
        }

        System.out.println("Rebuilt function: " + s);

        return s;
    }

    public static void convertArgumentToCode(UPLCompiler compiler, String argument)
    {
        List<ObjectToken> arguments = Tokeniser.tokenise(argument);

        if(arguments.get(0).tokenType == Tokeniser.TokenType.WORD && arguments.get(1).tagMatches(new String[]{"SPECIAL", "EXACT", "("}))
        {
            String s = rebuildFunction(arguments);

            convertFunctionToCode(compiler, Tokeniser.tokenise("@TEMP1@ = " + s));
            compiler.writeCode("psh @TEMP1@");
        }
        else
        {
            if(arguments.get(0).tokenType == Tokeniser.TokenType.WORD || arguments.get(0).tokenType == Tokeniser.TokenType.ARRAY_ACCESS || arguments.get(0).tokenType == Tokeniser.TokenType.NUMBER)
            {
                compiler.writeCode("psh " + arguments.get(0).toCodeValue());
            }
            else
            {
                if(arguments.get(0).tokenType == Tokeniser.TokenType.STRING)
                {
                    compiler.writeCode("dwd @TEMP1@ " + arguments.get(0).toCodeValue());
                    compiler.writeCode("psh @TEMP1@");
                }
            }
        }

        //TODO: equations
    }
}
