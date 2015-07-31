package call.upl.compiler.core;

import call.upl.compiler.node.CompileNode;
import call.upl.compiler.node.CompileStateData;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import call.upl.core.value.Value;
import cub3d.file.main.FileAPI;
import cub3d.file.reader.BasicReader;
import cub3d.file.writer.BasicWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Callum on 23/04/2015.
 */
public class UPLCompiler
{
    public static UPLCompiler instance = null;

    public static final boolean DEBUG = false;

    public List<String> code = new ArrayList<String>();

    public BasicWriter writer;

    public CompileStateData currentLineData = null;

    public UPLCompiler(FileAPI a) throws IOException
    {
        instance = this;

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

            //TODO: check for comment

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
        currentLineData = new CompileStateData();
        currentLineData.curLine = s;
        currentLineData.curLineNumber = i;

        CompileNode.attemptCompile(this, currentLineData);

        if(currentLineData.curLineNumber < i)
        {
            ExceptionSystem.throwException(s);
        }

        return currentLineData.curLineNumber;
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
