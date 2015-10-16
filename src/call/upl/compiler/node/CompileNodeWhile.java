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
public class CompileNodeWhile extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        //while ( s == d ) -> { }
        PatternBuilder while_ = new PatternBuilder();

        while_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.EXACT, "while");
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "(");

        while_.startOr();
        while_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.NUMBER, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.ARRAY_ACCESS, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);
        while_.endOr();

        while_.startOr();
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "==");
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "!=");
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "\\>");
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "\\>=");
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "\\<");
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "\\<=");
        while_.endOr();

        while_.startOr();
        while_.add(Tokeniser.TokenType.STRING, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.NUMBER, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.ARRAY_ACCESS, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        while_.add(Tokeniser.TokenType.FUNCTION_CALL, PatternMatcher.MatchType.ANY);
        while_.endOr();

        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, ")");
        while_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "-\\>");

        if(PatternMatcher.match(compileStateData, while_))
        {
            ObjectToken argument1 = tokens.get(2);
            ObjectToken argument2 = tokens.get(4);

            String conditionType = tokens.get(3).toCodeValue();

            writeCode("whl " + argument1.toCodeValue() + " " + conditionType + " " + argument2.toCodeValue());

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
                            ExceptionSystem.throwCodeException("While statement read past EOF, no end token found");
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
                ExceptionSystem.throwCodeException("While statement code block missing, no start token found");
            }

            writeCode("endwhl");

            return true;
        }

        return false;
    }
}
