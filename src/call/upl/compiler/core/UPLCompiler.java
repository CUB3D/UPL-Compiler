package call.upl.compiler.core;

import call.upl.compiler.node.CompileNode;
import call.upl.compiler.node.CompileStateData;
import call.upl.compiler.pattern.Pattern;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import call.upl.core.UPLUtils;
import call.upl.core.Value;
import com.sun.javafx.css.CssError;
import cub3d.file.main.FileAPI;
import cub3d.file.reader.BasicReader;
import cub3d.file.writer.BasicWriter;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Callum on 23/04/2015.
 */
public class UPLCompiler
{
    public static final boolean DEBUG = false;

    public BasicReader reader;

    public List<String> code = new ArrayList<String>();
    public Map<String, Value> values = new HashMap<String, Value>();

    public BasicWriter writer;

    public UPLCompiler(FileAPI a) throws IOException
    {
        this.reader = new BasicReader(a.getReader());

        FileAPI f = new FileAPI(a.getPath().toString().replaceAll(".call", ".o"));

        f.createFile();

        this.writer = new BasicWriter(f.getWriter());

        process();
    }

    public void process() throws IOException
    {
        readCode();
        runCode();
    }

    private void readCode()
    {
        try
        {
            BasicReader br = new BasicReader(reader);

            String s;

            while((s = br.readLine()) != null)
            {
                if(!s.trim().isEmpty())
                    code.add(s);
            }

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void runCode() throws IOException
    {
        for(int i = 0; i < code.size(); i++)
        {
            String s = code.get(i);

            i = execLine(s, i);
        }

        writer.cleanup();
    }

    private int execLine(String s, int i) throws IOException
    {
        CompileStateData compileStateData = new CompileStateData();
        compileStateData.curLine = s;
        compileStateData.curLineNumber = i;

        if(CompileNode.attemptCompile(this, compileStateData))
        {
            return compileStateData.curLineNumber;
        }

        PatternBuilder setText = new PatternBuilder();

        setText.addMatchAnyWord();
        setText.addMatchSpace(0);
        setText.addMatchExact("=");
        setText.addMatchSpace(0);
        setText.addMatchExact("\"");
        setText.addMatchSkipToExact("\"");

        if(PatternMacher.match(s, setText.toString()))
        {
            // x = "hgfd hgd hgfd"
            String[] ss = s.split("=");
            // x , "hgfd hgd hgfd"
            ss[0] = ss[0].replaceAll(" ", "");
            // x, "hgfd hgd hgfd"
            ss[1] = ss[1].replaceAll(" \"", "");
            ss[1] = ss[1].replaceAll("\"", "");
            // x, hdfd hgd hgfd

            writer.writeLine("dwd " + ss[0] + " " + ss[1]);
            values.put(ss[0], new Value(ss[1]));
        }


        // a = b + c
        PatternBuilder add = new PatternBuilder();

        add.addMatchAnyWord();
        add.addMatchSpace(0);
        add.addMatchExact("=");
        add.addMatchSpace(0);
        add.addMatchValue();
        add.addMatchSpace(0);
        add.addMatchSkipChar(); // add matchexact one of +, -, *, /
        add.addMatchSpace(0);
        add.addMatchValue();

        if(PatternMacher.match(s, add.toString())) // a = 10 + 20
        {
            s = s.replaceAll(" ", "");

            String operand = "";

            if(s.contains("+"))
            {
                operand = "\\+";
            } else
                if(s.contains("-"))
                {
                    operand = "-";
                } else
                    if(s.contains("*"))
                    {
                        operand = "\\*";
                    } else
                        if(s.contains("/"))
                        {
                            operand = "/";
                        }

            System.out.println(operand);

            String[] adds = s.split(operand);

            // a = 10, 20

            String[] ss = adds[0].split("=");

            //ss: a, 10

            adds[0] = adds[0].replaceAll(ss[0] + "=", "");

            if(operand.equals("\\+"))
            {
                writer.writeLine("add " + adds[0] + " " + adds[1]);
                writer.writeLine("pop " + ss[0]);
            }

            if(operand.equals("-"))
            {
                writer.writeLine("sub " + adds[0] + " " + adds[1]);
                writer.writeLine("pop " + ss[0]);
            }

            if(operand.equals("\\*"))
            {
                writer.writeLine("mul " + adds[0] + " " + adds[1]);
                writer.writeLine("pop " + ss[0]);
            }

            if(operand.equals("/"))
            {
                writer.writeLine("div " + adds[0] + " " + adds[1]);
                writer.writeLine("pop " + ss[0]);
            }
        }

        //print(x)
        PatternBuilder printVar = new PatternBuilder();
        printVar.addMatchAnyWord();
        printVar.addMatchSpace(0);
        printVar.addMatchExact("(");
        printVar.addMatchSpace(0);
        printVar.addMatchAnyWord();
        printVar.addMatchSpace(0);
        printVar.addMatchExact(")");

        if(PatternMacher.match(s, printVar.toString()))
        {
            // print ( x )
            s = s.replaceAll(" ", "");
            // print(x)
            s = s.replaceAll("\\(", " ");
            // print x)
            s = s.replaceAll("\\)", "");
            // print x
            String[] strings = s.split(" ");
            String methodName = strings[0];

            if(methodName.startsWith("print"))
            {
                String varName = strings[1];

                writer.writeLine("psh " + varName);
                writer.writeLine("int 0x1");

                if(methodName.equals("println"))
                {
                    writer.writeLine("dwd @TEMP0@ /n");
                    writer.writeLine("psh @TEMP0@");
                    writer.writeLine("int 0x1");
                    writer.writeLine("dwd @TEMP0@ NULL");
                }
            }
        }

        PatternBuilder printNum = new PatternBuilder();
        printNum.addMatchAnyWord();
        printNum.addMatchSpace(0);
        printNum.addMatchExact("(");
        printNum.addMatchSpace(0);

        printNum.addMatchNumber();

        printNum.addMatchSpace(0);
        printNum.addMatchExact(")");

        if(PatternMacher.match(s, printNum.toString()))
        {
            // print ( 10 )
            s = s.replaceAll(" ", "");
            // print(10)
            s = s.replaceAll("\\(", " ");
            // print 10)
            s = s.replaceAll("\\)", "");
            // print 10
            String[] strings = s.split(" ");
            String methodName = strings[0];

            if(methodName.startsWith("print"))
            {
                String text = strings[1];

                writer.writeLine("psh " + text);
                writer.writeLine("int 0x1");

                if(methodName.equals("println"))
                {
                    writer.writeLine("dwd @TEMP0@ /n");
                    writer.writeLine("psh @TEMP0@");
                    writer.writeLine("int 0x1");
                    writer.writeLine("dwd @TEMP0@ NULL");
                }
            }
        }

        PatternBuilder printStr = new PatternBuilder();
        printStr.addMatchAnyWord();
        printStr.addMatchSpace(0);
        printStr.addMatchExact("(");
        printStr.addMatchSpace(0);

        printStr.addMatchExact("\"");
        printStr.addMatchSkipToExact("\"");

        printStr.addMatchSpace(0);
        printStr.addMatchExact(")");

        if(PatternMacher.match(s, printStr.toString()))
        {
            // print ( "X Y" )
            s = s.replaceAll("\\(", " ");
            // print   "X Y" )
            s = s.replaceAll("\\)", "");
            // print   "X Y" .
            s = s.trim();
            // print   "X Y".
            String[] strings = s.split(" ");
            String methodName = strings[0];

            if (methodName.startsWith("print"))
            {
                s = s.replaceAll(methodName + " ", "");
                // .  "X Y".
                s = s.trim();
                // "X Y"
                s = s.replaceAll("\"", "");
                // X Y

                if (methodName.equals("println"))
                {
                    s += "/n";
                }

                writer.writeLine("dwd @TEMP0@ " + s);
                writer.writeLine("psh @TEMP0@");
                writer.writeLine("int 0x1");
                writer.writeLine("dwd @TEMP0@ NULL");
            }

            if (methodName.equals("__UPLBC"))
            {
                s = s.replaceAll(methodName + " ", "");
                // .  "X Y".
                s = s.trim();
                // "X Y"
                s = s.replaceAll("\"", "");
                // X Y

                writer.writeLine(s);
            }
        }

        //if ( s == d ) -> { }
        PatternBuilder if_ = new PatternBuilder();

        if_.addMatchExact("if");
        if_.addMatchSpace(0);
        if_.addMatchExact("(");

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);

        if_.addMatchExact("=="); // todo multipul matches

        if_.addMatchSpace(0);
        if_.addMatchValue();
        if_.addMatchSpace(0);
        if_.addMatchExact(")");

        if_.addMatchSpace(0);
        if_.addMatchExact("-\\>");

        if(PatternMacher.match(s, if_.toString()))
        {
            //if(x == y) ->
            // if x == y
            s = s.replaceAll(" ", "");
            //if(x==y)->
            s = s.replaceAll("\\(", " ");
            //if x==y)->
            s = s.replaceAll("\\)", "");
            //if x==y->
            s = s.replaceAll("->", "");
            //if x==y
            s = s.replaceAll("==", " == ");

            writer.writeLine(s);

            i++;

            if(code.get(i).equals("{"))
            {
                i++;

                while(true)
                {
                    String line = code.get(i);

                    if(line.equals("}"))
                    {
                        break;
                    } else
                    {
                        String codeLine = line.trim();

                        i = execLine(codeLine, i);
                    }

                    i++;
                }
            } else
            {
                System.out.println("Error uncomplete statement");
            }

            writer.writeLine("endif");
        }

        //func x ( void ) ->
        PatternBuilder func = new PatternBuilder();
        func.addMatchExact("func");
        func.addMatchSpace(0);
        func.addMatchAnyWord();
        func.addMatchSpace(0);
        func.addMatchExact("(");
        func.addMatchSkipToExact(")");
        func.addMatchSpace(0);
        func.addMatchExact("-\\>");

        if(PatternMacher.match(s, func.toString()))
        {
            //def x ( x, y ) ->
            s = s.replaceAll("func", ".");
            //. x ( x, y ) ->
            s = s.replaceAll(" ", "");
            //.x(x,y)->
            s = s.replaceAll("\\(", " ");
            //.x x,y)->
            s = s.replaceAll("\\)->", "");
            // .x x,y
            String[] split = s.split(" ");
            String name = split[0];
            String[] args = split[1].split(",");

            writer.writeLine(name);

            name = name.replaceFirst("\\.", "");

            i++;

            if(code.get(i).equals("{"))
            {
                i++;

                for(int i1 = 0; i1 < args.length; i1++)
                {
                    writer.writeLine("pop " + (name + "@" + args[i1]));
                }

                while(true)
                {
                    String line = code.get(i);

                    if(line.equals("}"))
                    {
                        break;
                    } else
                    {
                        String codeLine = line.trim();


                        codeLine += " ";

                        for(int i1 = 0; i1 < args.length; i1++)
                        {
                            codeLine = codeLine.replaceAll(" " + args[i1] + " ", " " + name + "@" + args[i1] + " ");
                            codeLine = codeLine.replaceAll("\\(" + args[i1], "(" + name + "@" + args[i1]);
                            codeLine = codeLine.replaceAll(args[i1] + "\\)", name + "@" + args[i1] + ")");
                        }

                        codeLine = codeLine.trim();

                        i = execLine(codeLine, i);
                    }

                    i++;
                }
            } else
            {
                System.out.println("Error uncomplete statement");
            }

            writer.writeLine("end");
        }

        PatternBuilder callFunc = new PatternBuilder();
        //helloWorld ( x, y )
        callFunc.addMatchAnyWord();
        callFunc.addMatchSpace(0);
        callFunc.addMatchExact("(");
        callFunc.addMatchSkipToExact(")");

        if(PatternMacher.match(s, callFunc.toString()))
        {
            //psh x, psh y, jmp name
            s = s.replaceAll(" ", "");
            //helloWorld(x,y)
            s = s.replaceAll("\\(", " ");
            //helloWorld x,y)
            s = s.replaceAll("\\)", "");
            //helloWorld x,y
            String[] strings = s.split(" ");
            String name = strings[0];
            String[] args = strings[1].split(",");

            for(int i1 = 0; i1 < args.length; i1++)
            {
                writer.writeLine("psh " + args[i1]);
            }

            writer.writeLine("jmp " + name);
        }

        return i;
    }

    public void writeCode(String s)
    {
        try
        {
            writer.writeLine(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
        FileAPI api = new FileAPI(args[0]);

        new UPLCompiler(api);
    }
}
