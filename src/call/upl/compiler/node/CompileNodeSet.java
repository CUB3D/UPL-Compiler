package call.upl.compiler.node;

import call.upl.compiler.core.FunctionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.NumberToken;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.core.tokeniser.WordToken;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeSet extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        //<WORD ANY><SPECIAL EXACT =><NUMBER ANY||WORD ANY||ARRAY_ACCESS ANY||STRING ANY>
        //x = 0

        PatternBuilder setVariableNumber = new PatternBuilder();
        setVariableNumber.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "=");
        setVariableNumber.startOr();
        setVariableNumber.add(Tokeniser.TokenType.NUMBER, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.ARRAY_ACCESS, PatternMatcher.MatchType.ANY);
        setVariableNumber.endOr();

        if(PatternMatcher.match(compileStateData, setVariableNumber))
        {
            String varName = ((WordToken) tokens.get(0)).getValue();

            ObjectToken valueToken = tokens.get(2);

            writeCode((valueToken.tokenType == Tokeniser.TokenType.STRING ? "dwd " + varName + " " + valueToken.toCodeValue() : "mov " + varName + " " + valueToken.toCodeValue()));

            return true;
        }

        //x = foo ( 10 )

        PatternBuilder setVariableFunction = new PatternBuilder();
        setVariableFunction.enableInexactMatching();
        setVariableFunction.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        setVariableFunction.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "=");
        setVariableFunction.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);

        if(PatternMatcher.match(compileStateData, setVariableFunction))
        {
            FunctionParser.convertFunctionToCode(uplCompiler, compileStateData.tokens);

            return true;
        }

        return false;
    }
}
