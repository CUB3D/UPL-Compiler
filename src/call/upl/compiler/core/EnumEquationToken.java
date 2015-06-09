package call.upl.compiler.core;

/**
 * Created by Callum on 30/05/2015.
 */
public enum EnumEquationToken
{
    ADD("add", "+", true, true, 2, true),
    SUBTRACT("sub", "-", false, true, 2, true),
    DIVIDE("div", "/", false, true, 3, true),
    MULTIPLY("mul", "*", true, true, 3, true),
    MODULUS("mod", "%", false, true, 3, true),
    OPENPAREN("", "(", false, false, 100, true),
    CLOSEPAREN("", ")", false, false, 100, true);


    private String opcode;
    private String identifier;
    private boolean escape;

    private int precedence;
    private boolean isLeftAssociative;

    private boolean isOperator;

    EnumEquationToken(String opcode, String identifier, boolean escape, boolean isOperator, int precedence, boolean isLeftAssociative)
    {
        this.opcode = opcode;
        this.identifier = identifier;
        this.escape = escape;
        this.precedence = precedence;
        this.isLeftAssociative = isLeftAssociative;
        this.isOperator = isOperator;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getIdentifierEscaped()
    {
        if(escape)
        {
            return "\\" + getIdentifier();
        }

        return getIdentifier();
    }

    public String getOpcode()
    {
        return opcode;
    }

    public int getPrecedence()
    {
        return precedence;
    }

    public boolean isLeftAssociative()
    {
        return isLeftAssociative;
    }

    public boolean isOperator()
    {
        return isOperator;
    }
}
