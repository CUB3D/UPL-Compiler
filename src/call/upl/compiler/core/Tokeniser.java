package call.upl.compiler.core;

import org.omg.CORBA.TRANSACTION_UNAVAILABLE;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 18/08/2015.
 */
public class Tokeniser
{
    public static List<String> tokenise(String s)
    {
        List<String> tokens = new ArrayList<String>();

        char[] characters = s.toCharArray();

        String tempToken = "";

        TokenType type = TokenType.UNKNOWN;

        for(int i = 0; i < characters.length; i++)
        {
            char currentCharacter = characters[i];

            if(type == TokenType.UNKNOWN)
            {
                type = getTokenType(currentCharacter);
            }

            if(getTokenType(currentCharacter) != type)
            {
                // split
                tokens.add(tempToken);
                tempToken = "";
                type = getTokenType(currentCharacter);
            }

            if(currentCharacter == ' ')
            {
                // also split but don't recognise
                tempToken = "";
                type = TokenType.UNKNOWN;
                continue;
            }

            if(isSpecial(currentCharacter))
            {
                if(!tempToken.isEmpty())
                {
                    tokens.add(tempToken);
                }
                tokens.add("" + currentCharacter);
                tempToken = "";
                type = TokenType.UNKNOWN;
                continue;
            }

            tempToken += characters[i];
        }

        if(!tempToken.isEmpty())
        {
            tokens.add(tempToken);
        }

        return tokens;
    }

    public static TokenType getTokenType(char c)
    {
        return Character.isLetter(c) ? TokenType.WORD :
                Character.isDigit(c) ? TokenType.NUMBER :
                        isSpecial(c) ? TokenType.SPECIAL : TokenType.UNKNOWN;
    }

    public static char[] special = {'=', '[', ']', '"', ':'};

    public static boolean isSpecial(char c)
    {
        for(char cc : special)
        {
            if(cc == c)
            {
                return true;
            }
        }

        return false;
    }


    enum TokenType
    {
        WORD, NUMBER, SPECIAL, UNKNOWN
    }
}
