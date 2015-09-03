package call.upl.compiler.core.tokeniser;

/**
 * Created by Callum on 03/09/2015.
 */
public class ObjectToken<T>
{
    public Tokeniser.TokenType tokenType;
    protected T value;

    public T getValue()
    {
        return value;
    }
}
