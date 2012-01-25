package com.inmobi.grid.usage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: srikanth.sundarrajan
 * Date: 8/19/11
 */
public class RMCParser {

    public static void main(String[] args) throws IOException {
        for (File file : new File("/data/rmc/").listFiles()) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
                BufferedReader reader = new BufferedReader(new FileReader(file));
                OutputStream writer = new FileOutputStream("/data/rmc/processed/" + file.getName());
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(" ");
                    String user = split[2];
                    String url = split[6];
                    if (user.equals("noc") || user.startsWith("shivu")) continue;
                    if (!url.startsWith("/reporting")) continue;
                    if (url.contains("image") || url.contains("js") || url.contains("css")) continue;

                    String[] urlargs = url.split("[&\\?]");
                    List<String> validargs = new ArrayList<String>();
                    for (int index = 1; index < urlargs.length; index++) {
                        String urlarg = urlargs[index];
                        if (!urlarg.endsWith("=") && !urlarg.endsWith("=yyyy/mm/dd")) {
                            validargs.add(urlarg);
                        }
                    }
                    Collections.sort(validargs);
                    String val = user + "|" + urlargs[0] + "|" + validargs;
                    System.out.println(val);
                    writer.write(val.getBytes());
                    writer.write('\n');
                }
                writer.close();
            }
        }
    }

}
