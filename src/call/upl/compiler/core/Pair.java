package call.upl.compiler.core;

/**
 * Created by Callum on 03/09/2015.
 */
public class Pair<A, B>
{
    public Pair(A first, B second)
    {
        this.first = first;
        this.second = second;
    }

    public A first;
    public B second;

    @Override
    public String toString()
    {
        return "First: " + first.toString() + " Second: " + second.toString();
    }
}
