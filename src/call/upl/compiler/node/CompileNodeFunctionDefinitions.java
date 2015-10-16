package call.upl.compiler.node;

import call.upl.compiler.core.ExceptionSystem;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.FunctionCallToken;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.List;
import java.util.SortedMap;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFunctionDefinitions extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        //func x ( void ) ->
        PatternBuilder func = new PatternBuilder();

        func.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.EXACT, "func");
        func.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);
        func.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "-\\>");

        if(PatternMatcher.match(compileStateData, func))
        {
            compileStateData.isInFunction = true;

            FunctionCallToken functionCallToken = (FunctionCallToken) tokens.get(1);

            String name = functionCallToken.getValue().first;

            List<ObjectToken> arguments = functionCallToken.getValue().second;

            writeCode("." + name);

            //enter namespace
            writeCode("nsp " + name);

            compileStateData.curLineNumber++;

            if(getLine(compileStateData.curLineNumber++).equals("{"))
            {
                if(!arguments.isEmpty())
                {
                    for (int argumentIndex = arguments.size() - 1; argumentIndex >= 0; argumentIndex--)
                    {
                        writeCode("pop " + arguments.get(argumentIndex).toCodeValue());
                    }
                }

                while(true)
                {
                    String line = getLine(compileStateData.curLineNumber);

                   // compileStateData.curLine = line;

                    if(line.equals("}"))
                    {
                        break;
                    }
                    else
                    {
                        if(getCurrentLineNumber() + 1 == uplCompiler.code.size())
                        {
                            // could not find end
                            ExceptionSystem.throwCodeException("Function statement read past EOF, no end token found, in function \"" + name + "\"");
                        }
                        else
                        {
                            compileStateData.curLineNumber = uplCompiler.execLine(line, compileStateData.curLineNumber);
                        }
                    }

                   compileStateData.curLineNumber++;
                }
            }
            else
            {
                ExceptionSystem.throwCodeException("Function code block missing, no start token found, in function \"" + name + "\"");
            }

            //leave namespace
            writeCode("endnsp");

            writeCode("end");

            compileStateData.curLineNumber++;

            compileStateData.isInFunction = false;

            return true;
        }


        return false;
    }
}
