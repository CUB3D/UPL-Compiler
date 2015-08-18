package call.upl.compiler.node;

import call.upl.compiler.core.ExceptionSystem;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFunctionDefinitions extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //func x ( void ) ->
        PatternBuilder func = new PatternBuilder();
        func.addMatchExact("func");
        func.addMatchSpace(0);
        func.addMatchAnyWord();
        func.addMatchSpace(0);
        func.addMatchExact("(");
        func.addMatchSkipToExact(")");
        func.addMatchSpace(0);
        func.addMatchExact("-\\>");

        if(PatternMacher.match(curLine, func.toString()))
        {
            compileStateData.isInFunction = true;

            //def x ( x, y ) ->
            curLine = curLine.replaceAll("func", ".");
            //. x ( x, y ) ->
            curLine = curLine.replaceAll(" ", "");
            //.x(x,y)->
            curLine = curLine.replaceFirst("\\(", " ");
            //.x x,y)->
            curLine = curLine.replaceAll("\\)->", "");
            // .x x,y
            String[] split = curLine.split(" ");
            String name = split[0];
            boolean hasArgs = split.length == 2;
            String[] arguments = null;

            if(hasArgs)
            {
                arguments = split[1].split(",");
            }

            writeCode(name);

            //enter namespace
            writeCode("nsp " + name.substring(1, name.length()));

            compileStateData.curLineNumber++;

            if(getLine(compileStateData.curLineNumber++).equals("{"))
            {
                if(hasArgs)
                {
                    for (int argumentIndex = arguments.length - 1; argumentIndex >= 0; argumentIndex--)
                    {
                        writeCode("pop " + arguments[argumentIndex]);
                    }
                }

                while(true)
                {
                    String line = getLine(compileStateData.curLineNumber);

                   // compileStateData.curLine = line;

                    if(line.equals("}"))
                    {
                        break;
                    }
                    else
                    {
                        if(getCurrentLineNumber() + 1 == uplCompiler.code.size())
                        {
                            // could not find end
                            ExceptionSystem.throwCodeException("Function statement read past EOF, no end token found, in function \"" + name + "\"");
                        }
                        else
                        {
                            compileStateData.curLineNumber = uplCompiler.execLine(line, compileStateData.curLineNumber);
                        }
                    }

                   compileStateData.curLineNumber++;
                }
            }
            else
            {
                ExceptionSystem.throwCodeException("Function code block missing, no start token found, in function \"" + name + "\"");
            }

            //leave namespace
            writeCode("endnsp");

            writeCode("end");

            compileStateData.curLineNumber++;

            compileStateData.isInFunction = false;

            return true;
        }


        return false;
    }
}
