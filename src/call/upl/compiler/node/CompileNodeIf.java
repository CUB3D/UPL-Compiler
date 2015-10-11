package call.upl.compiler.node;

import call.upl.compiler.core.ExceptionSystem;
import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;

import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeIf extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        //if ( s == d ) -> { }
        PatternBuilder if_ = new PatternBuilder();

        if_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.EXACT, "if");
        if_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "(");

        if_.startOr();
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.NUMBER, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.ARRAY_ACCESS, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);
        if_.endOr();

        if_.startOr();
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.EXACT, "==");
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.EXACT, "!=");
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.EXACT, "\\>");
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.EXACT, "\\>=");
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.EXACT, "\\<");
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.EXACT, "\\<=");
        if_.endOr();

        if_.startOr();
        if_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.NUMBER, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.ARRAY_ACCESS, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        if_.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);
        if_.endOr();

        if_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, ")");
        if_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "-\\>");

        System.out.println(if_.toString());


        if(PatternMatcher.match(compileStateData, if_))
        {
            //if(x == y) ->
            // if x == y
            curLine = curLine.replaceAll(" ", "");
            //if(x==y)->
            curLine = curLine.replaceAll("\\(", " ");
            //if x==y)->
            curLine = curLine.replaceAll("\\)", "");
            //if x==y->
            curLine = curLine.replaceAll("->", "");
            //if x==y
            curLine = curLine.replaceAll("==", " == ");
            curLine = curLine.replaceAll(">", " > ");
            curLine = curLine.replaceAll("<", " < ");

            writeCode(curLine);

            compileStateData.curLineNumber++;

            if(getLine(compileStateData.curLineNumber++).equals("{"))
            {
                while(true)
                {
                    String line = getLine(compileStateData.curLineNumber);

                    if(line.equals("}"))
                    {
                        break;
                    }
                    else
                    {
                        if(getCurrentLineNumber() + 1 == uplCompiler.code.size())
                        {
                            // could not find end
                            ExceptionSystem.throwCodeException("If statement read past EOF, no end token found");
                        }
                        else
                        {
                            compileStateData.curLineNumber = uplCompiler.execLine(line, getCurrentLineNumber());
                        }
                    }

                    compileStateData.curLineNumber++;
                }
            }
            else
            {
                ExceptionSystem.throwCodeException("If statement code block missing, no start token found");
            }

            writeCode("endif");

            return true;
        }

        return false;
    }
}
