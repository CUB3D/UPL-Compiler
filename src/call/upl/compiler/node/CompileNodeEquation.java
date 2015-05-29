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
        PatternBuilder add = new PatternBuilder();

        add.addMatchAnyWord();
        add.addMatchSpace(0);
        add.addMatchExact("=");
        add.addMatchSpace(0);
        add.addMatchSkipToExact(Character.toString(curLine.toCharArray()[curLine.length() - 1]));

        //TODO: better pattern recognition

        if(PatternMacher.match(curLine, add.toString()))
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

        return false;
    }
}
