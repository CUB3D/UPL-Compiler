package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;

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
    public static final CompileNodeWhile COMPILE_NODE_WHILE = new CompileNodeWhile();
    public static final CompileNodeArray COMPILE_NODE_ARRAY = new CompileNodeArray();
    public static final CompileNodeFor COMPILE_NODE_FOR = new CompileNodeFor();

    public CompileNode()
    {
        compileNodes.add(this);
    }

    abstract boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens);

    public static boolean attemptCompile(UPLCompiler compiler, CompileStateData compileStateData)
    {
        for(CompileNode compileNode : compileNodes)
        {
            if(compileNode.compile(compiler, compileStateData, compileStateData.curLine, compileStateData.tokens))
            {
                return true;
            }
        }

        return false;
    }

    public void writeCode(String s)
    {
        UPLCompiler.instance.writeCode(s);
    }

    public String getLine(int line)
    {
        return UPLCompiler.instance.code.get(line).trim();
    }

    public int getCurrentLineNumber()
    {
        return UPLCompiler.instance.currentLineData.curLineNumber;
    }
}
