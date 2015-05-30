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

        basicExpression.addMatchAnyWord();
        basicExpression.addMatchSpace(0);
        basicExpression.addMatchExact("=");
        basicExpression.addMatchSpace(0);
        basicExpression.addMatchSkipToExact(Character.toString(curLine.toCharArray()[curLine.length() - 1]));

        //TODO: better pattern recognition

        if(PatternMacher.match(curLine, basicExpression.toString()))
        {
            if(curLine.contains("\"")) // not equation
            {
                return false;
            }

            if(!(curLine.contains("+") || curLine.contains("-") || curLine.contains("/") || curLine.contains("*"))) // is a set not an equation
            {
                return false;
            }

            ExpressionParser.convertExpressionToCode(curLine, uplCompiler);

            return true;
        }

        //a[0] = (5 + 4) / 3

        PatternBuilder setArray = new PatternBuilder();

        setArray.addMatchAnyWord();
        setArray.addMatchExact("[");
        setArray.addMatchValue();
        setArray.addMatchExact("]");
        setArray.addMatchSpace(0);
        setArray.addMatchExact("=");
        setArray.addMatchSpace(0);
        setArray.addMatchSkipToExact(Character.toString(curLine.toCharArray()[curLine.length() - 1]));

        if(PatternMacher.match(curLine, setArray.toString()))
        {
            if(!(curLine.contains("+") || curLine.contains("-") || curLine.contains("/") || curLine.contains("*"))) // is a set not an equation
            {
                return false;
            }

            ExpressionParser.convertExpressionToCode(curLine, uplCompiler);

            return true;
        }

        return false;
    }
}
