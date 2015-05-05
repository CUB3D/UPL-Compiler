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

    public int execLine(String s, int i)
    {
        CompileStateData compileStateData = new CompileStateData();
        compileStateData.curLine = s;
        compileStateData.curLineNumber = i;

        if(CompileNode.attemptCompile(this, compileStateData))
        {
            return compileStateData.curLineNumber;
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

                writeCode("psh " + varName);
                writeCode("int 0x1");

                if(methodName.equals("println"))
                {
                    writeCode("dwd @TEMP0@ /n");
                    writeCode("psh @TEMP0@");
                    writeCode("int 0x1");
                    writeCode("dwd @TEMP0@ NULL");
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

                writeCode("psh " + text);
                writeCode("int 0x1");

                if(methodName.equals("println"))
                {
                    writeCode("dwd @TEMP0@ /n");
                    writeCode("psh @TEMP0@");
                    writeCode("int 0x1");
                    writeCode("dwd @TEMP0@ NULL");
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

                writeCode("dwd @TEMP0@ " + s);
                writeCode("psh @TEMP0@");
                writeCode("int 0x1");
                writeCode("dwd @TEMP0@ NULL");
            }

            if (methodName.equals("__UPLBC"))
            {
                s = s.replaceAll(methodName + " ", "");
                // .  "X Y".
                s = s.trim();
                // "X Y"
                s = s.replaceAll("\"", "");
                // X Y

                writeCode(s);
            }
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
