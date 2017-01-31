package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;

import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeFor extends CompileNode
{
    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        //for ( value : array ) -> { }
        PatternBuilder for_ = new PatternBuilder();

        for_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.EXACT, "for");
        for_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "(");
        for_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        for_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, ":");
        for_.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        for_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, ")");
        for_.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "-\\>");

//        for_.addMatchExact("for");
//        for_.addMatchSpace(0);
//        for_.addMatchExact("(");
//        for_.addMatchSpace(0);
//        for_.addMatchVariable();
//        for_.addMatchSpace(0);
//        for_.addMatchExact(":");
//        for_.addMatchSpace(0);
//        for_.addMatchVariable();
//        for_.addMatchSpace(0);
//        for_.addMatchExact(")");
//        for_.addMatchSpace(0);
//        for_.addMatchExact("-\\>");

        if(PatternMatcher.match(compileStateData, for_))
        {
            curLine = curLine.replaceAll(" ", "");
            //for(x:y)->
            curLine = curLine.replaceAll("for\\(", "");
            //x:y)->
            curLine = curLine.replaceAll("\\)->", "");
            //x:y

            String[] variables = curLine.split(":");

            //x, y

            writeCode("mov @TEMP@ 0");
            writeCode("mov @TEMP1@ 0");
            writeCode("mov " + variables[0] + " 0");

            writeCode("psh " + variables[1]);
            writeCode("int 0x6");
            writeCode("pop @TEMP1@");

            writeCode("whl @TEMP@ < @TEMP1@");

            writeCode("mov " + variables[0] + " " + variables[1] + "[@TEMP@]");

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
                        String codeLine = line;

                        compileStateData.curLineNumber = uplCompiler.execLine(codeLine, compileStateData.curLineNumber);
                    }

                    compileStateData.curLineNumber++;
                }
            }
            else
            {
                System.out.println("Error uncompleted statement");
            }

            writeCode("add @TEMP@ 1");
            writeCode("pop @TEMP@");

            writeCode("endwhl");

            return true;
        }

        return false;
    }
}
