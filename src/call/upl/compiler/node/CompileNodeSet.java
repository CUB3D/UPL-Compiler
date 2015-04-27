package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import call.upl.core.Value;

import java.math.BigDecimal;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeSet extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        PatternBuilder setNum = new PatternBuilder();

        setNum.addMatchAnyWord();
        setNum.addMatchSpace(0);
        setNum.addMatchExact("=");
        setNum.addMatchSpace(0);
        setNum.addMatchNumber();

        if(PatternMacher.match(curLine, setNum.toString()))
        {
            curLine = curLine.replaceAll(" ", "");
            String[] ss = curLine.split("=");

            uplCompiler.writeCode("mov " + ss[0] + " " + ss[1]);
            uplCompiler.values.put(ss[0], new Value(new BigDecimal(ss[1])));

            return true;
        }

        return false;
    }
}
