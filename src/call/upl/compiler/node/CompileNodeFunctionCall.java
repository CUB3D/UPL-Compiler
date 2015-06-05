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
        callFunc.addSkipToEnd();

        if(PatternMacher.match(curLine, callFunc.toString()))
        {
            String name = curLine.substring(0, curLine.indexOf("(")).trim();

            if(!name.startsWith("_")) //TODO: __uplbc goes here
            {
                FunctionParser.convertFunctionToCode(uplCompiler, curLine);

                return true;
            }
        }

        return false;
    }
}
