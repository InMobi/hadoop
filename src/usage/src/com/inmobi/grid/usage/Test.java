package com.inmobi.grid.usage;

import com.sun.mail.mbox.FileInterface;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileChecksum;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * User: srikanth.sundarrajan
 * Date: 9/2/11
 */
public class Test {

    public static void main1(String[] args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

        Date dt = new Date(1314834905298L);
        System.out.println(format.format(dt));
        System.out.println(format1.format(dt));
    }

    public static void main2(String[] args) throws IOException {
        FileSystem fs = FileSystem.get(new Configuration());
        FileStatus[] statuses = fs.globStatus(new Path("/data/rr/2011-11-12*/*/*/*/*/*/*"));
        for (FileStatus status : statuses) {
            long s = System.nanoTime();
            FileChecksum cs = fs.getFileChecksum(status.getPath());
            long e = (System.nanoTime() - s) / (1000000);
            System.out.println(e + "ms - "  + status.getPath() + " ; " + cs);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(new File("/data/4028cba62f1f1e04012f2de8b82800c2.txt")));
        String pattern = "\"rq-guid\":";
        String line = "";

        FileOutputStream out = new FileOutputStream("/data/click-mobclix-4028cba62f1f1e04012f2de8b82800c2-11-22-00.txt");
        while ((line = in.readLine()) != null) {
            int index = line.indexOf(pattern);
            if (index > 0) {
                String impID = line.substring(index+11, index + 47);
                UUID id = UUID.fromString(impID);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                out.write((impID + "\u0001" + format.format(new Date(id.timestamp())) + "\u0001" + line + "\n").getBytes());
            }
        }

        out.close();
        in.close();
    }

    public static void main3(String[] args) {
        String[] s = new String[28];
        int i = 0;
        s[i++] = "7d1974d0-0133-1000-d0d1-000201730003";
        s[i++] = "7d25125a-0133-1000-c4da-000201730003";
        s[i++] = "7d2943fd-0133-1000-d4cf-000201730003";
        s[i++] = "7d386362-0133-1000-c7d3-000201730003";
        s[i++] = "7d474920-0133-1000-c838-000201740003";
        s[i++] = "7d4e9693-0133-1000-f47e-000101740003";
        s[i++] = "7d5791f1-0133-1000-f6da-000201730003";
        s[i++] = "7d5f6b69-0133-1000-e361-000401720003";
        s[i++] = "7d641ef5-0133-1000-d836-000201740003";
        s[i++] = "7d654b19-0133-1000-cbdb-000301720003";
        s[i++] = "7d65d3b2-0133-1000-f53c-000201740003";
        s[i++] = "7d6d4f06-0133-1000-d3d7-000201730003";
        s[i++] = "7d8d2078-0133-1000-e7dc-000301720003";
        s[i++] = "7d93ebef-0133-1000-cace-000201730003";
        s[i++] = "7db26a84-0133-1000-c5d6-000201730003";
        s[i++] = "7db3becd-0133-1000-e6cb-000201730003";
        s[i++] = "7dbab80e-0133-1000-d280-000201740003";
        s[i++] = "7de074af-0133-1000-f17e-000101740003";
        s[i++] = "7df43dcf-0133-1000-c77e-000101740003";
        s[i++] = "7df78ed3-0133-1000-f02c-000501730003";
        s[i++] = "7e0300d2-0133-1000-f97e-000101740003";
        s[i++] = "7e0f01a0-0133-1000-dd2c-000501730003";
        s[i++] = "7e21a3a7-0133-1000-e180-000201740003";
        s[i++] = "7e254ab2-0133-1000-ee39-000601730003";
        s[i++] = "7e32942b-0133-1000-fe81-000201740003";
        s[i++] = "7e3b1fe5-0133-1000-c582-000201740003";
        s[i++] = "7e3ccd5a-0133-1000-d52c-000501730003";
        s[i++] = "c7b74605-0133-1000-d03f-000113630003";

        for (String s1 : s) {
            UUID id = UUID.fromString(s1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            System.out.println(format.format(new Date(id.timestamp())));
        }
    }
}
