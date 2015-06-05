package call.upl.compiler.node;

import call.upl.compiler.core.FunctionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeSet extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        PatternBuilder setArray = new PatternBuilder();
        setArray.addMatchVariable();
        setArray.addMatchSpace(0);
        setArray.addMatchExact("=");
        setArray.addMatchSpace(0);
        setArray.addMatchVariable();

        if(PatternMacher.match(curLine, setArray.toString()))
        {
            String[] args = curLine.split("=");
            String name = args[0].trim();
            String value = args[1].trim();

            uplCompiler.writeCode("mov " + name + " " + value);

            return true;
        }

        PatternBuilder setText = new PatternBuilder();

        setText.addMatchVariable();
        setText.addMatchSpace(0);
        setText.addMatchExact("=");
        setText.addMatchSpace(0);
        setText.addMatchExact("\"");
        setText.addMatchSkipToExact("\"");

        if(PatternMacher.match(curLine, setText.toString()))
        {
            // x = "hgfd hgd hgfd "
            String[] ss = curLine.split("=");
            // x , "hgfd hgd hgfd "
            ss[0] = ss[0].replaceAll(" ", "");
            // x, "hgfd hgd hgfd "
            ss[1] = ss[1].replaceAll("\"", "");
            // x, hdfd hgd hgfd .

            ss[1] = ss[1].substring(1, ss[1].length());

            uplCompiler.writeCode("dwd " + ss[0] + " " + ss[1]);

            return true;
        }

        PatternBuilder setFunc = new PatternBuilder();

        //x = foo ( 10 )

        setFunc.addMatchVariable();
        setFunc.addMatchSpace(0);
        setFunc.addMatchExact("=");
        setFunc.addMatchSpace(0);
        setFunc.addMatchAnyWord();
        setFunc.addMatchSpace(0);
        setFunc.addMatchExact("(");
        setFunc.addSkipToEnd();

        if(PatternMacher.match(curLine, setFunc.toString()))
        {
            String name = curLine.substring(curLine.indexOf("="), curLine.indexOf("(")).trim();


            if(!name.startsWith("_"))
            {
                FunctionParser.convertFunctionToCode(uplCompiler, curLine);
            }

            return true;
        }

        return false;
    }
}
