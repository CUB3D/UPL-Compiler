package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFunctionDefinitions extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        //func x ( void ) ->
        PatternBuilder func = new PatternBuilder();
        func.addMatchExact("func");
        func.addMatchSpace(0);
        func.addMatchAnyWord();
        func.addMatchSpace(0);
        func.addMatchExact("(");
        func.addMatchSkipToExact(")");
        func.addMatchSpace(0);
        func.addMatchExact("-\\>");

        if(PatternMacher.match(curLine, func.toString()))
        {
            //def x ( x, y ) ->
            curLine = curLine.replaceAll("func", ".");
            //. x ( x, y ) ->
            curLine = curLine.replaceAll(" ", "");
            //.x(x,y)->
            curLine = curLine.replaceAll("\\(", " ");
            //.x x,y)->
            curLine = curLine.replaceAll("\\)->", "");
            // .x x,y
            String[] split = curLine.split(" ");
            String name = split[0];
            boolean hasArgs = split.length == 2;
            String[] args = null;

            if(hasArgs)
            {
                args = split[1].split(",");
            }

            uplCompiler.writeCode(name);

            name = name.replaceFirst("\\.", "");

            int i = compileStateData.curLineNumber + 1;

            if(uplCompiler.code.get(i).equals("{"))
            {
                i++;

                if(hasArgs)
                {
                    for (int i1 = 0; i1 < args.length; i1++)
                    {
                        uplCompiler.writeCode("pop " + (name + "@" + args[i1]));
                    }
                }

                while(true)
                {
                    String line = uplCompiler.code.get(i);

                    if(line.equals("}"))
                    {
                        break;
                    } else
                    {
                        String codeLine = line.trim();


                        codeLine += " ";

                        if(hasArgs)
                        {
                            for (int i1 = 0; i1 < args.length; i1++)
                            {
                                codeLine = codeLine.replaceAll(" " + args[i1] + " ", " " + name + "@" + args[i1] + " ");
                                codeLine = codeLine.replaceAll("\\(" + args[i1], "(" + name + "@" + args[i1]);
                                codeLine = codeLine.replaceAll(args[i1] + "\\)", name + "@" + args[i1] + ")");
                            }
                        }

                        codeLine = codeLine.trim();

                        i = uplCompiler.execLine(codeLine, i);
                    }

                    i++;
                }
            } else
            {
                System.out.println("Error uncomplete statement");
            }

            compileStateData.curLineNumber = i;

            uplCompiler.writeCode("end");

            return true;
        }


        return false;
    }
}
