package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;

import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeInclude extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        //include "Test .call"
        PatternBuilder include = new PatternBuilder();

        include.addMatchExact("include");
        include.addMatchSpace(0);
        include.addMatchExact("\"");
        include.addMatchSkipToExact("\"");

        if(PatternMatcher.match(curLine, include.toString()))
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
