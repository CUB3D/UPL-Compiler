package call.upl.compiler.core;

/**
 * Created by Callum on 09/06/2015.
 */
public class ExceptionSystem
{

    public static void throwCodeException(String s)
    {
        UPLCompiler instance = UPLCompiler.instance;

        System.out.println("Error: " + s + ", on line " + instance.currentLineData.curLineNumber);

        System.exit(0);
    }

    public static void throwException(String s)
    {
        System.out.println("Error " + s);

        System.exit(0);
    }
}
