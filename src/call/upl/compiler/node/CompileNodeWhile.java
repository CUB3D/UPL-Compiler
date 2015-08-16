package call.upl.compiler.node;

import call.upl.compiler.core.ExceptionSystem;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeWhile extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //while ( s == d ) -> { }
        PatternBuilder if_ = new PatternBuilder();

        if_.addMatchExact("while");
        if_.addMatchSpace(0);
        if_.addMatchExact("(");

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);

        if_.addMatchExact("==", "\\>", "\\<");

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);
        if_.addMatchExact(")");

        if_.addMatchSpace(0);
        if_.addMatchExact("-\\>");

        if(PatternMacher.match(curLine, if_.toString()))
        {
            curLine = curLine.replaceAll(" ", "");
            //while(x==y)->
            curLine = curLine.replaceAll("\\(", " ");
            //while x==y)->
            curLine = curLine.replaceAll("\\)", "");
            //while x==y->
            curLine = curLine.replaceAll("->", "");
            //while x==y
            curLine = curLine.replaceAll("==", " == ");
            curLine = curLine.replaceAll(">", " > ");
            curLine = curLine.replaceAll("<", " < ");

            curLine = curLine.replaceAll("while", "whl");

            writeCode(curLine);

            compileStateData.curLineNumber++;

            if(getLine(compileStateData.curLineNumber++).equals("{"))
            {
                while(true)
                {
                    String line = getLine(compileStateData.curLineNumber);

                    if(line.equals("}"))
                    {
                        break;
                    }
                    else
                    {
                        if(getCurrentLineNumber() + 1 == uplCompiler.code.size())
                        {
                            // could not find end
                            ExceptionSystem.throwCodeException("While statement read past EOF, no end token found");
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
                ExceptionSystem.throwCodeException("While statement code block missing, no start token found");
            }

            writeCode("endwhl");

            compileStateData.curLineNumber++;

            return true;
        }

        return false;
    }
}
