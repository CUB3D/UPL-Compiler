package call.upl.compiler.test;

import java.util.List;

/**
 * Created by Callum on 19/08/2015.
 */
public class TestUtils
{
    public static String join(List<?> stringList)
    {
        String returnValue = "";

        for(Object s : stringList)
        {
            returnValue += s.toString() + ",";
        }

        return returnValue.substring(0, returnValue.length() - 1);
    }
}
