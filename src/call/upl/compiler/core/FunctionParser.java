package call.upl.compiler.core;

import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 05/06/2015.
 */
public class FunctionParser
{
    @Deprecated
    public static List<String> breakdownFunction(String function)
    {
        //x = foo()
        //foo()

        String[] args = function.split("=");

        String fucntionCall;
        String returnValue = null;

        if(args.length > 1)
        {
            fucntionCall = args[1];
            returnValue = args[0];
        }
        else
        {
            fucntionCall = args[0];
        }

        List<String> ret = new ArrayList<String>();

        ret.add(returnValue);

        breakdownFunctionCall(ret, fucntionCall.trim());

        return ret;
    }

    @Deprecated
    public static void breakdownFunctionCall(List<String> ret, String functioncall)
    {
        //foo (x [ n ], x, x ( n ))
        String name = functioncall.substring(0, functioncall.indexOf("("));

        functioncall = functioncall.replaceFirst(name, "");

        ret.add(name.trim());

        //(x [ n ], x, x ( n ))

        functioncall = functioncall.substring(1, functioncall.length() - 1);

        //x [ n ], x, x ( n )

        String[] arguments = functioncall.split(",");

        ret.add("" + arguments.length);

        for(int i = 0; i < arguments.length; i++)
        {
            ret.add(arguments[i].trim());
        }

        //x [ n ]
        //x
        //x ( n )
    }

    public static void convertFunctionToCode(UPLCompiler compiler, List<ObjectToken> function)
    {
        //foo = bar("foobar", 20)

        String returnValue = function.get(0).toCodeValue(); // could be a WORD or ARRAY_ACCESS type

        String functionName = function.get(2).toCodeValue(); // will always be a WORD type

        for(int i = 4; i < function.size() - 1; i++) // could change to i += 2 to remove if
        {
            ObjectToken token = function.get(i);

            if(token.tokenType != Tokeniser.TokenType.SPECIAL)
            {
                convertArgumentToCode(compiler, function, i);
            }
        }

        compiler.writeCode("jmp " + functionName);

        if(returnValue != null)
        {
            compiler.writeCode("pop " + returnValue);
        }
    }

    public static void convertArgumentToCode(UPLCompiler compiler, List<ObjectToken> arguments, int index)
    {
        ObjectToken argument = arguments.get(index);

        if(argument.tokenType == Tokeniser.TokenType.WORD || argument.tokenType == Tokeniser.TokenType.ARRAY_ACCESS || argument.tokenType == Tokeniser.TokenType.NUMBER)
        {
            compiler.writeCode("psh " + argument.toCodeValue());
        }

        if(argument.tokenType == Tokeniser.TokenType.STRING)
        {
            compiler.writeCode("dwd @TEMP1@ " + argument.toCodeValue());
            compiler.writeCode("psh @TEMP1@");
        }

        //TODO: functionCalls, equations
    }
}
