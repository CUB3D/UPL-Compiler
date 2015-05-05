package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public abstract class CompileNode
{
    private static List<CompileNode> compileNodes = new ArrayList<CompileNode>();

    public static final CompileNodeSet COMPILE_NODE_SET = new CompileNodeSet();
    public static final CompileNodeEquation COMPILE_NODE_EQUATION = new CompileNodeEquation();
    public static final CompileNodeIf COMPILE_NODE_IF = new CompileNodeIf();
    public static final CompileNodeFunctionCall COMPILE_NODE_FUNCTION_CALL = new CompileNodeFunctionCall();

    public CompileNode()
    {
        compileNodes.add(this);
    }

    abstract boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine);

    public static boolean attemptCompile(UPLCompiler compiler, CompileStateData compileStateData)
    {
        for(CompileNode compileNode : compileNodes)
        {
            if(compileNode.compile(compiler, compileStateData, compileStateData.curLine))
            {
                return true;
            }
        }

        return false;
    }
}
