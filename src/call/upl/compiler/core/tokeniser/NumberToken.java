package call.upl.compiler.core.tokeniser;

import java.math.BigDecimal;

/**
 * Created by Callum on 03/09/2015.
 */
public class NumberToken extends ObjectToken<BigDecimal>
{
    public NumberToken(BigDecimal decimal)
    {
        this.tokenType = Tokeniser.TokenType.NUMBER;
        this.value = decimal;
    }

    @Override
    public boolean tagMatches(String[] s)
    {
        if(!super.tagMatches(s))
            return false;

        String mode = s[1];

        if(mode.equals("ANY"))
        {
            return true;
        }
        else
        {
            if(mode.equals("EXACT"))
            {
                return value.equals(new BigDecimal(s[2]));
            }
        }

        return false;
    }

    @Override
    public String toCodeValue()
    {
        return value.toPlainString();
    }
}
