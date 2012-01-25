package com.inmobi.grid.lib;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiFileTextInputFormat 
    extends MultiFileInputFormat<LongWritable, Text> {

    @Override
    public RecordReader<LongWritable,Text> getRecordReader(InputSplit split
        , JobConf job, Reporter reporter) throws IOException {
      return new MultiFileLineRecordReader(job, (MultiFileSplit)split);
    }

  /**
   * RecordReader is responsible from extracting records from the InputSplit.
   * This record reader accepts a {@link MultiFileSplit}, which encapsulates several
   * files, and no file is divided.
   */
  public static class MultiFileLineRecordReader
    implements RecordReader<LongWritable, Text> {

    private MultiFileSplit split;
    private long offset; //total offset read so far;
    private long totLength;
    private FileSystem fs;
    private int count = 0;
    private Path[] paths;

    private FSDataInputStream currentStream;
    private BufferedReader currentReader;

    public MultiFileLineRecordReader(Configuration conf, MultiFileSplit split)
      throws IOException {

      this.split = split;
      fs = FileSystem.get(conf);
      this.paths = split.getPaths();
      this.totLength = split.getLength();
      this.offset = 0;

      //open the first file
      Path file = paths[count];
      currentStream = fs.open(file);
      currentReader = new BufferedReader(new InputStreamReader(currentStream));
    }

    public void close() throws IOException { }

    public long getPos() throws IOException {
      long currentOffset = currentStream == null ? 0 : currentStream.getPos();
      return offset + currentOffset;
    }

    public float getProgress() throws IOException {
      return ((float)getPos()) / totLength;
    }

    public boolean next(LongWritable key, Text value) throws IOException {
      if(count >= split.getNumPaths())
        return false;

      /* Read from file, fill in key and value, if we reach the end of file,
       * then open the next file and continue from there until all files are
       * consumed.
       */
      String line;
      do {
        line = currentReader.readLine();
        if(line == null) {
          //close the file
          currentReader.close();
          offset += split.getLength(count);

          if(++count >= split.getNumPaths()) //if we are done
            return false;

          //open a new file
          Path file = paths[count];
          currentStream = fs.open(file);
          currentReader=new BufferedReader(new InputStreamReader(currentStream));
        }
      } while(line == null);
      //update the key and value
      key.set(0);
      value.set(line);

      return true;
    }

    public LongWritable createKey() {
      return new LongWritable();
    }

    public Text createValue() {
      return new Text();
    }
  }
}