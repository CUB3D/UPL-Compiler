package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFor extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //for ( value : array ) -> { }
        PatternBuilder for_ = new PatternBuilder();

        for_.addMatchExact("for");
        for_.addMatchSpace(0);
        for_.addMatchExact("(");
        for_.addMatchSpace(0);
        for_.addMatchVariable();
        for_.addMatchSpace(0);
        for_.addMatchExact(":");
        for_.addMatchSpace(0);
        for_.addMatchVariable();
        for_.addMatchSpace(0);
        for_.addMatchExact(")");
        for_.addMatchSpace(0);
        for_.addMatchExact("-\\>");

        if(PatternMacher.match(curLine, for_.toString()))
        {
            curLine = curLine.replaceAll(" ", "");
            //for(x:y)->
            curLine = curLine.replaceAll("for\\(", "");
            //x:y)->
            curLine = curLine.replaceAll("\\)->", "");
            //x:y

            String[] variables = curLine.split(":");

            //x, y

            uplCompiler.writeCode("mov @TEMP@ 0");
            uplCompiler.writeCode("mov @TEMP1@ 0");
            uplCompiler.writeCode("mov " + variables[0] + " 0");

            uplCompiler.writeCode("psh " + variables[1]);
            uplCompiler.writeCode("int 0x6");
            uplCompiler.writeCode("pop @TEMP1@");

            uplCompiler.writeCode("whl @TEMP@ < @TEMP1@");

            uplCompiler.writeCode("mov " + variables[0] + " " + variables[1] + "[@TEMP@] ");

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
                System.out.println("Error uncompleted statement");
            }

            compileStateData.curLineNumber = i;

            uplCompiler.writeCode("add @TEMP@ 1");
            uplCompiler.writeCode("pop @TEMP@");

            uplCompiler.writeCode("endwhl");

            return true;
        }

        return false;
    }
}
