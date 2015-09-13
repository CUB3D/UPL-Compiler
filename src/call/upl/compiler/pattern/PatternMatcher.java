package call.upl.compiler.pattern;

import call.upl.compiler.core.UPLCompiler;
import call.upl.compiler.core.tokeniser.NumberToken;
import call.upl.compiler.core.tokeniser.ObjectToken;
import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.node.CompileStateData;
import call.upl.core.UPL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 23/04/2015.
 */
public class PatternMatcher
{
    public static boolean match(CompileStateData csd, PatternBuilder builder)
    {
        return match(csd, builder.toString());
    }

    public static boolean match(CompileStateData csd, String pattern)
    {
        List<String> tags = getTags(pattern);

        for(int i = 0; i < csd.tokens.size(); i++)
        {
            String tag = tags.get(i);
            ObjectToken token = csd.tokens.get(i);

            if(tag.contains("||"))
            {
                String[] orTags = tag.split("\\|\\|");

                boolean or = false;

                for(String s : orTags)
                {
                    if (token.tagMatches(s.split(" ")))
                    {
                        or = true;
                        break;
                    }
                }

                if(or)
                {
                    //found a match
                    continue;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if (!token.tagMatches(tags.get(i).split(" ")))
                {
                    return false;
                }
            }
        }

        return true;
    }


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

        if(UPLCompiler.DEBUG)
        {
            System.out.println("CC: " + csd.curChar + "  TLEN: " + csd.text.length + ", TXT: " + text);
        }

        if(csd.curChar < csd.text.length) // some of the text was unmached
        {
            if(UPLCompiler.DEBUG)
            {
                System.out.println("Pattern match failed: unmatched text");
            }

            return false;
        }

        if(UPLCompiler.DEBUG)
        {
            System.out.println("Pattern matched succesfuly");
        }

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

    public enum MatchType
    {
        ANY, EXACT;
    }
}
