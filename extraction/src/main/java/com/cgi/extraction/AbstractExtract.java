package com.cgi.extraction;

import com.cgi.extraction.exception.AucunActiveLinkException;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractExtract {

    private final static char CR = (char) 0x0D;
    private final static char LF = (char) 0x0A;
    private final static String CRLF = "" + CR + LF;
    private final static String UTF8 = "char-set: UTF-8" + CRLF;

    private static Pattern patternChangeDiary;
    private static Matcher matcherChangeDiary;

    private static Pattern patternName;
    private static Matcher matcherName;

    private String input = "../tests/webService.def";
    private String output = "./output/%s.def";
    protected String start = "";

    public void extract ()
    throws AucunActiveLinkException
    {

        int index = 1;
        patternChangeDiary = Pattern.compile("change-diary");
        patternName = Pattern.compile("name");
        String line;
        String name = "";
        BufferedWriter writer = null;
        String tmp = "";
        int tour = 0;


        try(BufferedReader reader = Files.newBufferedReader(Paths.get(input), StandardCharsets.UTF_8)) {
            while((line = reader.readLine()) !=null )
            {
                if ( line.startsWith(start)) {

                    tmp += UTF8 + line + CRLF;
                    while ((line = reader.readLine()) != null) {
                        matcherName = patternName.matcher(line);
                        if(matcherName.find() && tmp != ""){
                            name = line.split(": ")[1]
                                    .replace(":","_")
                                    .replace("?","")
                                    .replace("*","")
                                    .replace("|","")
                                    .replace("<","")
                                    .replace(">","")
                                    .replace("/","")
                                    .replace("\"","")
                                    .replace("\\","") ;

                            writer = Files.newBufferedWriter(Paths.get(String.format(output,name)),StandardCharsets.UTF_8);
                            tmp +=  line + CRLF;
                            writer.write(tmp);
                            tmp = "";
                            tour ++;
                            continue;
                        }
                        matcherChangeDiary = patternChangeDiary.matcher(line);
                        if (!matcherChangeDiary.find())
                            writer.write(line + CRLF);

                        if (line.startsWith("end")) {
                            writer.close();
                            writer = null;
                            break;
                        }
                    }
                }
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tour == 0)
            throw new AucunActiveLinkException();
    }


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }


}
