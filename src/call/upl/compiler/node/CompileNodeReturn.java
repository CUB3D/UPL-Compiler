package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeReturn extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //return x
        PatternBuilder ret = new PatternBuilder();
        ret.addMatchExact("return");
        ret.addMatchSpace(0);
        ret.addMatchValue();

        if(PatternMacher.match(curLine, ret.toString()))
        {
            curLine = curLine.replaceFirst("return", "");

            curLine = "@TEMP0@ = " + curLine;

            uplCompiler.execLine(curLine, 0);

            uplCompiler.writeCode("psh @TEMP0@");

            return true;
        }

        return false;
    }
}
