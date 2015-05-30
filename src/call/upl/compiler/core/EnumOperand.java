package call.upl.compiler.core;

/**
 * Created by Callum on 30/05/2015.
 */
public enum EnumOperand
{
    ADD("add", "+", true),
    SUBTRACT("sub", "-"),
    DIVIDE("div", "/"),
    MULTIPLY("mul", "*", true),
    MODULUS("mod", "%");

    private String opcode;
    private String identifier;
    private boolean escape;

    EnumOperand(String opcode, String identifier)
    {
        this(opcode, identifier, false);
    }

    EnumOperand(String opcode, String identifier, boolean escape)
    {
        this.opcode = opcode;
        this.identifier = identifier;
        this.escape = escape;
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
}
