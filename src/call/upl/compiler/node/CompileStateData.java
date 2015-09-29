package call.upl.compiler.node;

import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;

import java.util.List;

/**
 * Created by Callum on 24/04/2015.
 */
public class CompileStateData
{
    public String curLine;
    public int curLineNumber;
    public boolean isInFunction;
    public List<ObjectToken> tokens;

    public void tokenise()
    {
        this.tokens = Tokeniser.tokenise(curLine);
    }
}
