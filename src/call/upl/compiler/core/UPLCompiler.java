package call.upl.compiler.core;

import call.upl.compiler.node.CompileNode;
import call.upl.compiler.node.CompileStateData;
import call.upl.compiler.pattern.Pattern;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import call.upl.core.UPL;
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

    public List<String> code = new ArrayList<String>();
    public Map<String, Value> values = new HashMap<String, Value>();

    public BasicWriter writer;

    public UPLCompiler(FileAPI a) throws IOException
    {
        FileAPI f = new FileAPI(a.getPath().toString().replaceAll(".call", ".o"));

        f.createFile();

        this.writer = new BasicWriter(f.getWriter());

        readCode(new BasicReader(a.getReader()));
        runCode();
    }

    public void readCode(String filename)
    {
        try
        {
            readCode(new BasicReader(new FileAPI(filename).getReader()));
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void readCode(BasicReader reader)
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

        // TODO: make this a node or add a special case in CompileNodeFunctionCall for special functions

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

            if (methodName.equals("__UPLBC"))
            {
                s = s.replaceAll(methodName + " ", "");
                // .  "X Y".
                s = s.trim();
                // "X Y"
                s = s.replaceAll("\"", "");
                // X Y

                writeCode(s);

                return compileStateData.curLineNumber;
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
        String exp = "answer = ((10 + 2) * (5 - 3)) / 4"; // = 6

        UPLCompiler uplCompiler = new UPLCompiler(new FileAPI(args[0]))
        {
            @Override
            public void readCode(String filename)
            {
            }

            @Override
            public void writeCode(String s)
            {
                System.out.println(s);
            }
        };

        ExpressionParser.convertExpressionToCode(exp, uplCompiler);

       // new UPLCompiler(api);
    }
}
