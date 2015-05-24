package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeWhile extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //while ( s == d ) -> { }
        PatternBuilder if_ = new PatternBuilder();

        if_.addMatchExact("while");
        if_.addMatchSpace(0);
        if_.addMatchExact("(");

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);

        if_.addMatchExact("==", "\\>", "\\<");

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);
        if_.addMatchExact(")");

        if_.addMatchSpace(0);
        if_.addMatchExact("-\\>");

        if(PatternMacher.match(curLine, if_.toString()))
        {
            curLine = curLine.replaceAll(" ", "");
            //while(x==y)->
            curLine = curLine.replaceAll("\\(", " ");
            //while x==y)->
            curLine = curLine.replaceAll("\\)", "");
            //while x==y->
            curLine = curLine.replaceAll("->", "");
            //while x==y
            curLine = curLine.replaceAll("==", " == ");
            curLine = curLine.replaceAll(">", " > ");
            curLine = curLine.replaceAll("<", " < ");

            curLine = curLine.replaceAll("while", "whl");

            uplCompiler.writeCode(curLine);

            int i = compileStateData.curLineNumber + 1;

            if(uplCompiler.code.get(i).trim().equals("{"))
            {
                i++;

                while(true)
                {
                    String line = uplCompiler.code.get(i).trim();

                    if(line.equals("}"))
                    {
                        break;
                    } else
                    {
                        String codeLine = line;

                        i = uplCompiler.execLine(codeLine, i);
                    }

                    i++;
                }
            } else
            {
                System.out.println("Error uncomplete statement");
            }

            compileStateData.curLineNumber = i;

            uplCompiler.writeCode("endwhl");

            return true;
        }

        return false;
    }
}
