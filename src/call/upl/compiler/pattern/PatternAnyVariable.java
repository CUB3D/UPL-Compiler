package call.upl.compiler.pattern;

/**
 * Created by Callum on 24/04/2015.
 */
public class PatternAnyVariable extends Pattern
{
    @Override
    public boolean matches(PatternStateData csd, String[] args)
    {
        boolean b = false;

        String s = "";

        while(true)
        {
            if(csd.curChar >= csd.text.length)
            {
                break;
            }

            char x = csd.text[csd.curChar];

            if(Character.toString(x).matches("[a-zA-Z_@0-9\\[\\]]"))
            {
                csd.curChar++;
                s += x;
                b = true;
            } else
            {
                break;
            }
        }

        if(!b)
        {
            return false;
        }
        else
        {
            String regx = "[a-zA-Z/_@0-9]+\\[?[a-zA-Z/_@0-9]+\\]?";

            if(s.matches(regx))
            {
                if(s.contains("\\["))
                {
                    if (s.contains("\\]"))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    if (s.contains("\\]"))
                    {
                        if (s.contains("\\["))
                        {
                            return true;
                        } else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return true;
                    }
                }
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public String getPatternID()
    {
        return "Variable";
    }
}
