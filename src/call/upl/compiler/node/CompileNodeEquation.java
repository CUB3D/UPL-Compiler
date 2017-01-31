package call.upl.compiler.node;

import call.upl.compiler.core.ExpressionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;

import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeEquation extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        // a = (b + c) / d
        PatternBuilder basicExpression = new PatternBuilder();

        basicExpression.addMatchVariable();
        basicExpression.addMatchSpace(0);
        basicExpression.addMatchExact("=");
        basicExpression.addMatchSpace(0);
        basicExpression.addSkipToEnd();

        if(PatternMatcher.match(curLine, basicExpression.toString()))
        {
            if(curLine.contains("\"") || curLine.contains(":")) // not equation
            {
                return false;
            }

            ExpressionParser.convertEquationToCode(curLine, uplCompiler);

            return true;
        }

        if(curLine.matches("[a-zA-Z_@0-9\\[\\]]+\\s*\\+\\+"))
        {
            String varName = curLine.replace("++", "");
            writeCode("add " + varName + " 1");
            writeCode("pop " + varName);

            return true;
        }

        return false;
    }
}
