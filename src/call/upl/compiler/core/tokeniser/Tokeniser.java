package call.upl.compiler.core.tokeniser;

import call.upl.compiler.core.Pair;
import org.omg.CORBA.TRANSACTION_UNAVAILABLE;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 18/08/2015.
 */
public class Tokeniser
{
    public static List<ObjectToken> convertStringTokenToObjectToken(List<String> tokens)
    {
        List<ObjectToken> objectTokens = new ArrayList<ObjectToken>();

        for(int i = 0; i < tokens.size(); i++)
        {
            String token = tokens.get(i);

            if(token.equals("\""))
            {
                Pair<String, Integer> reconstructedString = reconstructString(tokens, i);

                String value = reconstructedString.first;
                i = reconstructedString.second;

                objectTokens.add(new StringToken(value));
                continue;
            }

            TokenType type = getTokenType(token.charAt(0));

            if(type == TokenType.WORD)
            {
                if(tokens.get(i + 1).equals("[") && tokens.get(i + 3).equals("]"))
                {
                    Pair<ArrayAccessToken, Integer> reconstructedArrayAccess = reconstructArrayAccess(tokens, i);

                    objectTokens.add(reconstructedArrayAccess.first);
                    i = reconstructedArrayAccess.second;
                    continue;
                }
            }

            switch(type)
            {
                case WORD:
                    objectTokens.add(new WordToken(token));
                    break;
                case NUMBER:
                    objectTokens.add(new NumberToken(new BigDecimal(token)));
                    break;
                case SPECIAL:
                    objectTokens.add(new OperatorToken(token));
            }
        }

        return objectTokens;
    }

    private static Pair<ArrayAccessToken, Integer> reconstructArrayAccess(List<String> tokens, int pos)
    {
        // token layout: variable,[,index,]

        String variable = tokens.get(pos);
        pos += 2; // skip over [
        String index = tokens.get(pos);
        pos++; // skip to ]

        return new Pair<>(new ArrayAccessToken(variable, index), pos);
    }

    private static Pair<String, Integer> reconstructString(List<String> tokens, int pos)
    {
        pos++;

        String result = "";

        for(;pos < tokens.size(); pos++)
        {
            String token = tokens.get(pos);

            if(token.equals("\""))
            {
                result = result.substring(0, result.length()-1); // remove trailing space
                break;
            }

            result += token;
            result += " ";
        }

        return new Pair<>(result, pos);
    }

    public static List<String> tokenise(String s)
    {
        List<String> tokens = new ArrayList<String>();

        char[] characters = s.toCharArray();

        String tempToken = "";

        TokenType type = TokenType.UNKNOWN;

        boolean inString = false;

        for(int i = 0; i < characters.length; i++)
        {
            char currentCharacter = characters[i];

            if(inString)
            {
                if(currentCharacter == ' ')
                {
                    tokens.add(tempToken);
                    tempToken = "";
                    type = TokenType.UNKNOWN;
                    continue;
                }

                if(isSpecial(currentCharacter))
                {
                    if(currentCharacter == '"')
                    {
                        inString = !inString;
                        tokens.add(tempToken);
                        tokens.add("\"");
                        tempToken = "";
                        type = TokenType.UNKNOWN;
                        continue;
                    }
                }

                tempToken += currentCharacter;

                continue;
            }

            if(type == TokenType.UNKNOWN)
            {
                type = getTokenType(currentCharacter);
            }

            if(getTokenType(currentCharacter) == TokenType.NUMBER && type == TokenType.WORD)
            {

            }
            else
            {
                if(getTokenType(currentCharacter) != type)
                {
                    // split
                    tokens.add(tempToken);
                    tempToken = "";
                    type = getTokenType(currentCharacter);
                }
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
                if(currentCharacter == '"')
                {
                    inString = !inString;
                }

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
        return (Character.isLetter(c) || c == '@') ? TokenType.WORD :
                Character.isDigit(c) ? TokenType.NUMBER :
                        isSpecial(c) ? TokenType.SPECIAL : TokenType.UNKNOWN;
    }

    public static char[] special = {'=', '-', '>', '<', '[', ']', '"', ':', '(', ')', ','};

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
        WORD, NUMBER, SPECIAL, STRING, ARRAY_ACCESS, UNKNOWN
    }
}
