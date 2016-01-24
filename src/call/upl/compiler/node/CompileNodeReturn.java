package call.upl.compiler.node;

import call.upl.compiler.core.FunctionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
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
        //return x
        PatternBuilder ret = new PatternBuilder();
        ret.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.EXACT, "return");
        ret.enableInexactMatching();
        ret.startOr();
        ret.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        ret.add(Tokeniser.TokenType.ARRAY_ACCESS, PatternMatcher.MatchType.ANY);
        ret.add(Tokeniser.TokenType.NUMBER, PatternMatcher.MatchType.ANY);
        ret.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);
        ret.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);
        ret.endOr();


        if(PatternMatcher.match(compileStateData, ret))
        {
            ObjectToken token = compileStateData.tokens.get(1);

            if(token.tokenType == Tokeniser.TokenType.FUNCTION_CALL)
            {
                compileStateData.curLine = compileStateData.curLine.replace("return", "@TEMP1@ = ");
                compileStateData.tokenise();
                FunctionParser.convertFunctionToCode(uplCompiler, compileStateData.tokens);
                writeCode("psh @TEMP1@");
            }
            else
            {
                if(token.tokenType == Tokeniser.TokenType.STRING)
                {
                    writeCode("dwd @TEMP1@ " + token.toCodeValue());
                    writeCode("psh @TEMP1@");
                } else
                {
                    writeCode("psh " + token.toCodeValue());
                }
            }

            return true;
        }

        return false;
    }
}
