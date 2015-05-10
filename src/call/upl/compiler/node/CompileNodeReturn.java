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
            curLine = curLine.trim();

            uplCompiler.writeCode("psh " + curLine);

            return true;
        }

        return false;
    }
}
