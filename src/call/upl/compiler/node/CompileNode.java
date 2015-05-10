package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;

import javax.swing.ComponentInputMap;
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
    public static final CompileNodeFunctionDefinitions COMPILE_NODE_FUNCTION_DEFINITIONS = new CompileNodeFunctionDefinitions();
    public static final CompileNodeInclude COMPILE_NODE_INCLUDE = new CompileNodeInclude();
    public static final CompileNodeReturn COMPILE_NODE_RETURN = new CompileNodeReturn();

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
