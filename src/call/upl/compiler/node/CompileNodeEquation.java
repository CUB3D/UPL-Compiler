package call.upl.compiler.node;

import call.upl.compiler.core.ExpressionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeEquation extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        // a = (b + c) / d
        PatternBuilder basicExpression = new PatternBuilder();

        basicExpression.addMatchVariable();
        basicExpression.addMatchSpace(0);
        basicExpression.addMatchExact("=");
        basicExpression.addMatchSpace(0);
        basicExpression.addSkipToEnd();

        if(PatternMacher.match(curLine, basicExpression.toString()))
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
            uplCompiler.writeCode("add " + varName + " 1");
            uplCompiler.writeCode("pop " + varName);

            return true;
        }

        return false;
    }
}
