package call.upl.compiler.node;

import call.upl.compiler.core.FunctionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFunctionCall extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        PatternBuilder callFunc = new PatternBuilder();
        //helloWorld ( x, y )
        callFunc.addMatchAnyWord();
        callFunc.addMatchSpace(0);
        callFunc.addMatchExact("(");
        callFunc.addMatchSkipToExact(")");

        if(PatternMacher.match(curLine, callFunc.toString()))
        {
            String name = curLine.substring(0, curLine.indexOf("(")).trim();

            if(!name.startsWith("_"))
            {
                FunctionParser.convertFunctionToCode(uplCompiler, curLine);

                return true;
            }
            else
            {
                if (name.equals("__UPLBC"))
                {
                    curLine = curLine.replaceAll(name, "");
                    // .  ("X Y").
                    curLine = curLine.trim();
                    // ("X Y")
                    curLine = curLine.replaceAll("\"", "");
                    curLine = curLine.substring(1, curLine.length() - 1);
                    // X Y

                    writeCode(curLine);
                }
            }
        }

        return false;
    }
}
