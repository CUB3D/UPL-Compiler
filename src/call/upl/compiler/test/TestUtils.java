package call.upl.compiler.test;

import java.util.List;

/**
 * Created by Callum on 19/08/2015.
 */
public class TestUtils
{
    public static String join(List<String> stringList)
    {
        String returnValue = "";

        for(String s : stringList)
        {
            returnValue += s + ",";
        }

        return returnValue.substring(0, returnValue.length() - 1);
    }
}
