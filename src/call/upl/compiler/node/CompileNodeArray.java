package call.upl.compiler.node;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.ArrayCreationToken;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMatcher;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Callum on 27/04/2015.
 */
public class CompileNodeArray extends CompileNode
{
    public PatternBuilder array = new PatternBuilder();

    public CompileNodeArray()
    {
        array.add(Tokeniser.TokenType.WORD, PatternMatcher.MatchType.ANY);
        array.add(Tokeniser.TokenType.SPECIAL, PatternMatcher.MatchType.EXACT, "=");
        array.add(Tokeniser.TokenType.ARRAY_CREATION, PatternMatcher.MatchType.ANY);
    }

    @Override
    boolean compile(UPLCompiler uplCompiler, CompileStateData compileStateData, String curLine, List<ObjectToken> tokens)
    {
        if(PatternMatcher.match(compileStateData, array))
        {
            String name = tokens.get(0).toCodeValue();

            ArrayCreationToken arrayCreationToken = (ArrayCreationToken) tokens.get(2);

            String size = arrayCreationToken.getValue().second;

            writeCode("ary " + name + " " + size);

            List<String> contentsArray = arrayCreationToken.getValue().first;

            if(!contentsArray.isEmpty())
            {
                for (int i = 0; i < contentsArray.size(); i++)
                {
                    writeCode("mov " + name + "[" + i + "] " + contentsArray.get(i));
                }
            }

            if(UPLCompiler.DEBUG)
            {
                System.out.println("Array created: Name: " + name + ", Content: " + contentsArray + ", Size: " + size);
            }

            return true;
        }

        return false;
    }
}
