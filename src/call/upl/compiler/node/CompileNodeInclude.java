package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.StringToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
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

        include.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.EXACT, "include");
        include.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);

        if(PatternMatcher.match(compileStateData, include))
        {
            StringToken includePath = (StringToken) tokens.get(1);

            uplCompiler.readCode(includePath.getValue());

            return true;
        }

        return false;
    }
}
