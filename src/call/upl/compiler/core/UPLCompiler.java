package call.upl.compiler.core;

import call.upl.compiler.core.tokeniser.Tokeniser;
import call.upl.compiler.node.CompileNode;
import call.upl.compiler.node.CompileStateData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 23/04/2015.
 */
public class UPLCompiler
{
    public static UPLCompiler instance = null;

    public static final boolean DEBUG = false;

    public List<String> code = new ArrayList<String>();

    public BufferedWriter writer;

    public CompileStateData currentLineData = null;

    public UPLCompiler(Path path) throws IOException
    {
        instance = this;

        Path output = Paths.get(path.toString().replace(".call", ".o"));
        if (!Files.exists(output))
        {
            Files.createFile(output);
        }

        this.writer = Files.newBufferedWriter(output);

        readCode(Files.newBufferedReader(path));
        runCode();
    }

    public void readCode(String filename)
    {
        try
        {
            Path path = Paths.get(filename);
            readCode(Files.newBufferedReader(path));
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void readCode(BufferedReader reader)
    {
        try
        {
            String s;

            while((s = reader.readLine()) != null)
            {
                if(!s.trim().isEmpty() && !s.trim().startsWith("#") && !s.trim().startsWith("//"))
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

        writer.flush();
        writer.close();
    }

    public int execLine(String s, int i)
    {
        currentLineData = new CompileStateData();
        currentLineData.curLine = s;
        currentLineData.curLineNumber = i;
        currentLineData.tokens = Tokeniser.tokenise(s);

        CompileNode.attemptCompile(this, currentLineData);

        // executing prior pieces of code is not allowed
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
            writer.write(s + System.lineSeparator());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {
        long startTime = System.nanoTime();

        Path path = Paths.get(args[0]);

        new UPLCompiler(path);
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Compile completed in " + totalTime/1000000 + "ms");
    }


}
