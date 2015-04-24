package call.upl.compiler.core;

import call.upl.compiler.pattern.Pattern;
import call.upl.compiler.pattern.PatternBuilder;
import call.upl.compiler.pattern.PatternMacher;
import cub3d.file.main.FileAPI;
import cub3d.file.reader.BasicReader;
import cub3d.file.writer.BasicWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callum on 23/04/2015.
 */
public class UPLCompiler
{
    public BasicReader reader;

    public List<String> code = new ArrayList<String>();

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
                    code.add(s.trim());
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void runCode() throws IOException
    {
        for(int i = 0; i < code.size(); i++)
        {
            String s = code.get(i);

            PatternBuilder set = new PatternBuilder();

            set.addMatchAnyWord();
            set.addMatchSpace(0);
            set.addMatchSkipChar();
            set.addMatchValue();

            System.out.println(set.toString());

            if(PatternMacher.match(s, set.toString()))
            {
                System.out.println("Test");
              //  s = s.replaceAll(" ", "");
              //  String[] ss = s.split("=");

             //   writer.writeLine("mov " + ss[0] + " " + ss[1]);
            }
/*
            if(PatternMacher.match(s, "#_?_@_?_@")) // a = 10 + 20
            {
                s = s.replaceAll(" ", "");
                String[] adds = s.split("\\+");

                // a = 10, 20

                String[] ss = adds[0].split("=");

                //ss: a, 10

                adds[0] = adds[0].replaceAll(ss[0] + "=", "");

                writer.writeLine("add " + adds[0] + " " + adds[1]); // add 10 20
                writer.writeLine("pop " + ss[0]); // pop a
            }*/
        }

        writer.cleanup();
    }

    public static void main(String[] args) throws IOException
    {
        FileAPI api = new FileAPI("E:\\Development\\Java\\UPL Compiler\\Test.call");

        new UPLCompiler(api);
    }
}
