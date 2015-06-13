package call.upl.compiler.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 05/06/2015.
 */
public class FunctionParser
{
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

    public static void convertFunctionToCode(UPLCompiler compiler, String function)
    {
        List<String> breakdown = breakdownFunction(function);

        String returnValue = breakdown.get(0);

        String functionName = breakdown.get(1);

        int argumentCount = Integer.parseInt(breakdown.get(2));

        for(int i = 0; i < argumentCount; i++)
        {
            String line = breakdown.get(3 + i);

            convertArgumentToCode(compiler, line);
        }

        compiler.writeCode("jmp " + functionName);

        if(returnValue != null)
        {
            compiler.writeCode("pop " + returnValue);
        }
    }

    public static void convertArgumentToCode(UPLCompiler compiler, String argument)
    {
        //x, x [ n ], x ( n, n )
        if(argument.trim().isEmpty())
            return;

        if(CompilerUtils.isVariable(argument) || CompilerUtils.isArrayAccess(argument))
        {
            compiler.writeCode("psh " + argument);
        }
        else
        {
            if(CompilerUtils.isString(argument))
            {
                compiler.execLine("@TEMP1@ = " + argument, 0);
                compiler.writeCode("psh @TEMP1@");
            }
            else
            {
                if(CompilerUtils.isFunction(argument))
                {
                    convertFunctionToCode(compiler, "@TEMP0@ = " + argument);
                    compiler.writeCode("psh @TEMP0@");
                }
                else
                {
                    ExpressionParser.convertEquationToCode("@TEMP2@ = " + argument, compiler);
                    compiler.writeCode("psh @TEMP2@");
                }
            }
        }
    }
}
