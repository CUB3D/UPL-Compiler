package call.upl.compiler.node;

import call.upl.compiler.core.FunctionParser;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.FunctionCallToken;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;
import com.sun.javafx.fxml.expression.Expression;

import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFunctionCall extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        PatternBuilder callFunc = new PatternBuilder();
        callFunc.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);

        if(PatternMatcher.match(compileStateData, callFunc))
        {
            FunctionCallToken function = (FunctionCallToken) compileStateData.tokens.get(0);

            String name = function.getValue().first;

            if(!name.startsWith("_")) // check for compiler function
            {
                compileStateData.curLine = "@TEMP1@ = " + compileStateData.curLine;
                compileStateData.tokens = Tokeniser.tokenise(compileStateData.curLine);

               FunctionParser.convertFunctionToCode(uplCompiler, compileStateData.tokens);

                return true;
            }
            else
            {
                if(name.equals("__UPLBC"))
                {
                    curLine = curLine.replaceAll(name, "");
                    // .  ("X Y").
                    curLine = curLine.trim();
                    // ("X Y")
                    curLine = curLine.replaceAll("\"", "");
                    curLine = curLine.substring(1, curLine.length() - 1);
                    // X Y

                    writeCode(curLine);

                    return true;
                }
            }
        }

        return false;
    }
}
