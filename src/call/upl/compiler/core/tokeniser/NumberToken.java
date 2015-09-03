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
}
