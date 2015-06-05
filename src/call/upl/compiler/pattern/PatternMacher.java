package call.upl.compiler.pattern;

import call.upl.compiler.core.UPLCompiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 23/04/2015.
 */
public class PatternMacher
{
    public static boolean match(String text, String pattern)
    {
        List<String> tags = getTags(pattern);

        PatternStateData csd = new PatternStateData();
        csd.text = text.toCharArray();
        csd.textString = text;

        for(String tag : tags)
        {
            if(csd.curChar >= csd.text.length)
            {
                return false;
            }

            String[] args = tag.split(" ");

            Pattern p = Pattern.getPattenFor(args[0]);

            System.arraycopy(args, 1, args, 0, args.length - 1);

            if(p != null)
            {
                if(!p.matches(csd, args))
                {
                    //System.out.println("Pattern match failed, TAG: " + tag + ", CURCHAR: " + csd.curChar + ", ACTCHAR: " + csd.text[csd.curChar]);

                    return false;
                }
            }
            else
            {
                System.out.println("No match found for tag: " + tag);
            }
        }

        System.out.println("CC: " + csd.curChar + "  TLEN: " + csd.text.length + ", TXT: " + text);

        if(csd.curChar < csd.text.length) // some of the text was unmached
        {
            System.out.println("Pattern match failed: unmatched text");

            return false;
        }

        System.out.println("Pattern matched succesfuly");

        return true;
    }

    public static List<String> getTags(String s)
    {
        List<String> tags = new ArrayList<String>();

        String curTag = "";
        boolean inTag = false;

        char[] chars = s.toCharArray();

        for(int i = 0; i < chars.length; i++)
        {
            char c = chars[i];

            //System.out.println("TAG: " + curTag + " INTAG: " + inTag + " C: " + c);

            if(c == '<') // open tag
            {
                if(i > 0)
                {
                    if(chars[i - 1] != '\\')
                    {
                        curTag = "";
                        inTag = true;
                    }
                }
                else
                {
                    curTag = "";
                    inTag = true;
                }
            }

            if(c == '>')
            {
                if(i > 0)
                {
                    if(chars[i - 1] != '\\')
                    {
                        if(inTag)
                        {
                            tags.add(curTag);
                        }

                        inTag = false;
                        curTag = "";
                    }
                }
                else // should never happen but just for a sanity check
                {
                    if(inTag)
                    {
                        tags.add(curTag);
                    }

                    inTag = false;
                    curTag = "";
                }
            }

            if(c != '\\')
            {
                if(c == '>')
                {
                    if(i > 0)
                    {
                        if(chars[i - 1] != '\\')
                        {
                            continue;
                        }
                    }
                    else
                    {
                        continue;
                    }
                }

                if(c == '<')
                {
                    if(i > 0)
                    {
                        if(chars[i - 1] != '\\')
                        {
                            continue;
                        }
                    }
                    else
                    {
                        continue;
                    }
                }

                curTag += c;
            }
        }

        return tags;
    }
}
