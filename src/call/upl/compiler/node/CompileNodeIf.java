package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import call.upl.core.Value;

import java.math.BigDecimal;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeIf extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //if ( s == d ) -> { }
        PatternBuilder if_ = new PatternBuilder();

        if_.addMatchExact("if");
        if_.addMatchSpace(0);
        if_.addMatchExact("(");

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);

        if_.addMatchExact("=="); // todo multipul matches

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);
        if_.addMatchExact(")");

        if_.addMatchSpace(0);
        if_.addMatchExact("-\\>");

        if(PatternMacher.match(curLine, if_.toString()))
        {
            //if(x == y) ->
            // if x == y
            curLine = curLine.replaceAll(" ", "");
            //if(x==y)->
            curLine = curLine.replaceAll("\\(", " ");
            //if x==y)->
            curLine = curLine.replaceAll("\\)", "");
            //if x==y->
            curLine = curLine.replaceAll("->", "");
            //if x==y
            curLine = curLine.replaceAll("==", " == ");

            uplCompiler.writeCode(curLine);

            int i = compileStateData.curLineNumber + 1;

            if(uplCompiler.code.get(i).equals("{"))
            {
                i++;

                while(true)
                {
                    String line = uplCompiler.code.get(i);

                    if(line.equals("}"))
                    {
                        break;
                    } else
                    {
                        String codeLine = line.trim();

                        i = uplCompiler.execLine(codeLine, i);
                    }

                    i++;
                }
            } else
            {
                System.out.println("Error uncomplete statement");
            }

            compileStateData.curLineNumber = i;

            uplCompiler.writeCode("endif");
        }

        return false;
    }
}
