package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFunctionCall extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        PatternBuilder callFunc = new PatternBuilder();
        //helloWorld ( x, y )
        callFunc.addMatchAnyWord();
        callFunc.addMatchSpace(0);
        callFunc.addMatchExact("(");
        callFunc.addMatchSkipToExact(")");

        if(PatternMacher.match(curLine, callFunc.toString()))
        {
            //psh x, psh y, jmp name
            curLine = curLine.replaceAll(" ", "");
            //helloWorld(x,y)
            curLine = curLine.replaceAll("\\(", " ");
            //helloWorld x,y)
            curLine = curLine.replaceAll("\\)", "");
            //helloWorld x,y
            String[] strings = curLine.split(" ");
            String name = strings[0];
            boolean hasArgs = strings.length == 2;
            String[] args = null;

            if(hasArgs)
            {
                args = strings[1].split(",");
            }

            if(!name.startsWith("_"))
            {
                if(hasArgs)
                {
                    for (int i1 = 0; i1 < args.length; i1++)
                    {
                        uplCompiler.writeCode("psh " + args[i1]);
                    }
                }

                uplCompiler.writeCode("jmp " + name);

                return true;
            }
        }

        return false;
    }
}
