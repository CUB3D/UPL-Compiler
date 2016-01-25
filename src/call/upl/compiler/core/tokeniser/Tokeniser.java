package call.upl.compiler.core.tokeniser;

import call.upl.compiler.core.Pair;
import call.upl.compiler.core.UPLCompiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 18/08/2015.
 */
public class Tokeniser
{
    public static List<ObjectToken> tokenise(String input)
    {
        return convertStringTokenToObjectTokens(tokeniseString(input));
    }

    public static List<ObjectToken> convertStringTokenToObjectTokens(List<String> tokens)
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

            if(type == TokenType.SPECIAL)
            {
                if((tokens.size() - i) >= 3)
                {
                    if (token.equals("[") && tokens.get(tokens.size() - 3).equals("]"))
                    {
                        Pair<ArrayCreationToken, Integer> reconstructedArrayCreation = reconstructArrayCreation(tokens, i);

                        objectTokens.add(reconstructedArrayCreation.first);
                        i = reconstructedArrayCreation.second;
                        continue;
                    }
                }
            }

            if(type == TokenType.WORD)
            {
                if((tokens.size() - i) >= 2)
                {
                    if(tokens.get(i + 1).equals("(") && tokens.get(tokens.size() - 1).equals(")"))
                    {
                        Pair<FunctionCallToken, Integer> reconstructedFunctionCall = reconstructFunctionCalls(tokens, i);

                        objectTokens.add(reconstructedFunctionCall.first);
                        i = reconstructedFunctionCall.second;
                        continue;
                    }
                }

                if((tokens.size() - i) >= 3)
                {
                    if(tokens.get(i + 1).equals("[") && tokens.get(i + 3).equals("]"))
                    {
                        Pair<ArrayAccessToken, Integer> reconstructedFunctionCall = reconstructArrayAccess(tokens, i);

                        objectTokens.add(reconstructedFunctionCall.first);
                        i = reconstructedFunctionCall.second;
                        continue;
                    }
                }
            }

            // special grouping

            if(type == TokenType.SPECIAL)
            {
                if((tokens.size() - i) >= 2)
                {
                    if (token.equals("=") && tokens.get(i + 1).equals("=")) // == (equality)
                    {
                        objectTokens.add(new SpecialToken("=="));
                        i++;
                        continue;
                    }

                    if (token.equals("-") && tokens.get(i + 1).equals(">")) // -> (block declaration)
                    {
                        objectTokens.add(new SpecialToken("->"));
                        i++;
                        continue;
                    }

                    if (token.equals(">") && tokens.get(i + 1).equals("=")) // >= (more than or equal to)
                    {
                        objectTokens.add(new SpecialToken(">="));
                        i++;
                        continue;
                    }

                    if (token.equals("<") && tokens.get(i + 1).equals("=")) // <= (less than or equal to)
                    {
                        objectTokens.add(new SpecialToken("<="));
                        i++;
                        continue;
                    }

                    if (token.equals("!") && tokens.get(i + 1).equals("=")) // != (not equals)
                    {
                        objectTokens.add(new SpecialToken("!="));
                        i++;
                        continue;
                    }
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
                    objectTokens.add(new SpecialToken(token));
            }
        }

        return objectTokens;
    }

    private static Pair<FunctionCallToken, Integer> reconstructFunctionCalls(List<String> tokens, int pos)
    {
        // layout word,(,arg,,,arg,,,)

        String functionName = tokens.get(pos++);

        pos++; // skip (

        String arguments = "";

        boolean isInString = false;

        if(!tokens.get(pos).equals(")")) // has arguments
        {
            for(; pos < tokens.size() - 1; pos++)
            {
                //TODO: not parsing string correctely
                arguments += tokens.get(pos);

                if(isInString) arguments += " ";

                if(tokens.get(pos).equals("\""))
                {
                    isInString = !isInString;

                    if(!isInString)
                    {
                        arguments = arguments.substring(0, arguments.length() - 3);
                        arguments += "\"";
                    }
                }
            }
        }

        if(UPLCompiler.DEBUG)
        {
            System.out.println("Found function: " + functionName + " with arguments: " + arguments);
        }

        pos++; // skip )

        return new Pair<>(new FunctionCallToken(functionName, arguments), pos);
    }

    private static Pair<ArrayCreationToken, Integer> reconstructArrayCreation(List<String> tokens, int pos)
    {
        // token layout: [,content | blank,],:,size

        pos++;

        List<String> contentTokens = new ArrayList<String>();
        String size = "";


        while(!tokens.get(pos).equals("]"))
        {
            String curArg = "";

            while(true)
            {
                curArg += tokens.get(pos);

                System.out.println(tokens.get(pos));

                pos++;

                if(tokens.get(pos).equals("]"))
                {
                    break;
                }

                if(tokens.get(pos++).equals(","))
                {
                    break;
                }
            }

            contentTokens.add(curArg);
        }

        for(String s : contentTokens){
            System.out.println("CONT: " + s);
        }

        pos ++; // skip ]

        if(tokens.get(pos++).equals(":")) // checking for this for a future feature
        {
            size = tokens.get(pos);
        }

        return new Pair<>(new ArrayCreationToken(contentTokens, size), pos);
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

    public static List<String> tokeniseString(String s)
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
        return (Character.isLetter(c) || c == '@' || c == '_') ? TokenType.WORD :
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


    public enum TokenType
    {
        WORD, NUMBER, SPECIAL, STRING, ARRAY_ACCESS, ARRAY_CREATION, FUNCTION_CALL, UNKNOWN
    }
}
