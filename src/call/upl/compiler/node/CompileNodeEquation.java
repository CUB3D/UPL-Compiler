package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import call.upl.core.Value;

import java.math.BigDecimal;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeEquation extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine)
    {
        // a = b + c
        PatternBuilder add = new PatternBuilder();

        add.addMatchAnyWord();
        add.addMatchSpace(0);
        add.addMatchExact("=");
        add.addMatchSpace(0);
        add.addMatchValue();
        add.addMatchSpace(0);
        add.addMatchSkipChar(); // add matchexact one of +, -, *, /
        add.addMatchSpace(0);
        add.addMatchValue();

        if(PatternMacher.match(curLine, add.toString())) // a = 10 + 20
        {
            curLine = curLine.replaceAll(" ", "");

            String operand = "";

            if(curLine.contains("+"))
            {
                operand = "\\+";
            } else
                if(curLine.contains("-"))
                {
                    operand = "-";
                } else
                    if(curLine.contains("*"))
                    {
                        operand = "\\*";
                    } else
                        if(curLine.contains("/"))
                        {
                            operand = "/";
                        }

            System.out.println(operand);

            String[] adds = curLine.split(operand);

            // a = 10, 20

            String[] ss = adds[0].split("=");

            //ss: a, 10

            adds[0] = adds[0].replaceAll(ss[0] + "=", "");

            if(operand.equals("\\+"))
            {
                uplCompiler.writeCode("add " + adds[0] + " " + adds[1]);
                uplCompiler.writeCode("pop " + ss[0]);
            }

            if(operand.equals("-"))
            {
                uplCompiler.writeCode("sub " + adds[0] + " " + adds[1]);
                uplCompiler.writeCode("pop " + ss[0]);
            }

            if(operand.equals("\\*"))
            {
                uplCompiler.writeCode("mul " + adds[0] + " " + adds[1]);
                uplCompiler.writeCode("pop " + ss[0]);
            }

            if(operand.equals("/"))
            {
                uplCompiler.writeCode("div " + adds[0] + " " + adds[1]);
                uplCompiler.writeCode("pop " + ss[0]);
            }

            return true;
        }

        return false;
    }
}
