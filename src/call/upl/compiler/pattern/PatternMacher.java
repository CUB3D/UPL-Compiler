package call.upl.compiler.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 23/04/2015.
 */
public class PatternMacher
{
    public static boolean match(String text, String pattern)
    {
        System.out.println(text);

        List<String> tags = getTags(pattern);

        PatternStateData csd = new PatternStateData();
        csd.text = text.toCharArray();

        for(String tag : tags)
        {
            // System.out.println("PAT: " + c + " ID: " + cur);

            String[] args = tag.split(" ");

            Pattern p = Pattern.getPattenFor(args[0]);

            System.out.println("Parse tag: " + args[0]);

            if(p != null)
            {
                if(!p.matches(csd))
                {
                    return false;
                }
            }
            else
            {
                System.out.println("No match found for tag: " + tag);
            }
        }

        if(csd.curChar < csd.text.length) // some of the text was unmached
            return false;

        System.out.println("True");

        return true;
    }

    public static List<String> getTags(String s)
    {
        List<String> tags = new ArrayList<String>();

        String curTag = "";
        boolean inTag = false;

        for(char c : s.toCharArray())
        {
           // System.out.println("TAG: " + curTag + " INTAG: " + inTag + " C: " + c);

            if(c == '<') // open tag
            {
                curTag = "";
                inTag = true;
            }

            if(c == '>')
            {
                if(inTag)
                    tags.add(curTag);

                inTag = false;
                curTag = "";
            }
            if(inTag && c != '>' && c != '<')
            {
                curTag += c;
            }
        }

        return tags;
    }
}
