package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;

import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeReturn extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        curLine = curLine.trim();

        //return x
        PatternBuilder ret = new PatternBuilder();
        ret.addMatchExact("return");
        ret.addMatchSpace(0);
        ret.addMatchVariable();

        if(PatternMatcher.match(curLine, ret.toString()))
        {
            curLine = curLine.replaceFirst("return", "");

            curLine = "@TEMP0@ = " + curLine;

            uplCompiler.execLine(curLine, compileStateData.curLineNumber);

            writeCode("psh @TEMP0@");

            return true;
        }

        return false;
    }
}
