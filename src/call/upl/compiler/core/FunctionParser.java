package call.upl.compiler.core;

import call.upl.compiler.core.tokeniser.FunctionCallToken;
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
        //foo = bar("foobar", 20)

        String returnValue = function.get(0).toCodeValue(); // could be a WORD or ARRAY_ACCESS type

        FunctionCallToken functionCall = (FunctionCallToken) function.get(2); // will always be a FUNCTION_CALL type

        String argumentString = "";

        for(int i = 0; i < functionCall.getValue().second.size(); i++) // could change to i += 2 to remove if
        {
            ObjectToken token = functionCall.getValue().second.get(i);

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

        compiler.writeCode("jmp " + functionCall.getValue().first);

        if(returnValue != null)
        {
            compiler.writeCode("pop " + returnValue);
        }
    }

    public static void convertArgumentToCode(UPLCompiler compiler, String argument)
    {
        List<ObjectToken> arguments = Tokeniser.tokenise(argument);

        if(arguments.get(0).tokenType == Tokeniser.TokenType.FUNCTION_CALL)
        {
            convertFunctionToCode(compiler, Tokeniser.tokenise("@TEMP1@ = " + argument));
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
