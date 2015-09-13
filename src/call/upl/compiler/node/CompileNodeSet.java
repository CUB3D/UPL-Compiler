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

        PatternBuilder setVariableNumber = new PatternBuilder();
        setVariableNumber.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "=");
        setVariableNumber.startOr();
        setVariableNumber.add(Tokeniser.TokenType.NUMBER, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);
        setVariableNumber.add(Tokeniser.TokenType.ARRAY_ACCESS, PatternMatcher.MatchType.ANY);
        setVariableNumber.endOr();

        System.out.println(PatternMatcher.match(compileStateData, setVariableNumber));

        if(PatternMatcher.match(compileStateData, setVariableNumber))
        {
            String varName = ((WordToken) tokens.get(0)).getValue();

            ObjectToken valueToken = tokens.get(2);

            writeCode((valueToken.tokenType == Tokeniser.TokenType.STRING ? "dwd " : "mov ") + varName + " " + valueToken.toCodeValue());
        }

        if(true)
            return true;

        PatternBuilder setArray = new PatternBuilder();
        setArray.addMatchVariable();
        setArray.addMatchSpace(0);
        setArray.addMatchExact("=");
        setArray.addMatchSpace(0);
        setArray.addMatchVariable();

        if(PatternMatcher.match(curLine, setArray.toString()))
        {
            String[] args = curLine.split("=");
            String name = args[0].trim();
            String value = args[1].trim();

            writeCode("mov " + name + " " + value);

            return true;
        }

        PatternBuilder setFunc = new PatternBuilder();

        //x = foo ( 10 )

        setFunc.addMatchVariable();
        setFunc.addMatchSpace(0);
        setFunc.addMatchExact("=");
        setFunc.addMatchSpace(0);
        setFunc.addMatchAnyWord();
        setFunc.addMatchSpace(0);
        setFunc.addMatchExact("(");
        setFunc.addSkipToEnd();

        if(PatternMatcher.match(curLine, setFunc.toString()))
        {
            String name = curLine.substring(curLine.indexOf("="), curLine.indexOf("(")).trim();


            if(!name.startsWith("_"))
            {
                FunctionParser.convertFunctionToCode(uplCompiler, curLine);
            }

            return true;
        }

        return false;
    }
}
