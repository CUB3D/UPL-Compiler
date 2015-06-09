package call.upl.compiler.core;

/**
 * Created by Callum on 30/05/2015.
 */
public enum EnumOperand
{
    ADD("add", "+", true, 2, true),
    SUBTRACT("sub", "-", false, 2, true),
    DIVIDE("div", "/", false, 3, true),
    MULTIPLY("mul", "*", true, 3, true),
    MODULUS("mod", "%", false, 3, true);

    private String opcode;
    private String identifier;
    private boolean escape;

    private int precedence;
    private boolean isLeftAssociative;

    EnumOperand(String opcode, String identifier, boolean escape, int precedence, boolean isLeftAssociative)
    {
        this.opcode = opcode;
        this.identifier = identifier;
        this.escape = escape;
        this.precedence = precedence;
        this.isLeftAssociative = isLeftAssociative;
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
}
