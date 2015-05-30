package call.upl.compiler.node;

import call.upl.compiler.core.ExpressionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import call.upl.core.UPLUtils;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeArray extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        PatternBuilder array = new PatternBuilder();

        array.addMatchAnyWord();
        array.addMatchSpace(0);
        array.addMatchExact("=");
        array.addMatchSpace(0);
        array.addMatchExact("[");
        array.addMatchSkipToExact("]");
        array.addMatchSpace(0);
        array.addMatchExact(":");
        array.addMatchSpace(0);
        array.addMatchValue();

        if(PatternMacher.match(curLine, array.toString()))
        {
            curLine = curLine.replaceAll(" ", "");

            String name = curLine.split("=")[0];
            String contents = curLine.split("=")[1];
            String size = contents.split(":")[1];
            contents = contents.substring(0, contents.length() - size.length() - 1);

            uplCompiler.writeCode("ary " + name + " " + size);

            contents = contents.substring(1, contents.length() - 1); // remove []

            String[] contentsArray = contents.split(",");

            if(contents.contains(","))
            {
                for (int i = 0; i < contentsArray.length; i++)
                {
                    uplCompiler.writeCode("mov " + name + "[" + i + "] " + contentsArray[i]);
                }
            }

            if(UPLCompiler.DEBUG)
            {
                System.out.println("Array created: Name: " + name + ", Content: " + contents + ", Size: " + size);
            }

            return true;
        }

        return false;
    }
}
