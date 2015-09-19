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

    public boolean tagMatches(String[] s)
    {
        return ("" + tokenType).equals(s[0]);
    }

    public String toCodeValue()
    {
        return value.toString();
    }

    @Override
    public String toString()
    {
        return toCodeValue();
    }
}
