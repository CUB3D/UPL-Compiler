package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeInclude extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //include "Test .call"
        PatternBuilder include = new PatternBuilder();

        include.addMatchExact("include");
        include.addMatchSpace(0);
        include.addMatchExact("\"");
        include.addMatchSkipToExact("\"");

        if(PatternMacher.match(curLine, include.toString()))
        {
            curLine = curLine.replaceFirst("include", "");
            curLine = curLine.trim();
            curLine = curLine.replaceAll("\"", "");

            uplCompiler.readCode(curLine);

            return true;
        }

        return false;
    }
}
