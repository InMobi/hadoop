package com.inmobi.grid.usage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class TestJob extends Configured implements Tool {
  public static Log LOG = LogFactory.getLog(TestJob.class);

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new TestJob(), args);
  }

  @Override
  public int run(String[] strings) throws Exception {
    Job job = new Job(getConf(), "log-test-job");
    job.getConfiguration().set("mapred.child.java.opts", "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005");
    job.getConfiguration().set("mapred.map.max.attempts", "40");
    job.setMapperClass(MyMapper.class);
    job.setOutputFormatClass(NullOutputFormat.class);
    job.setNumReduceTasks(0);
    job.setInputFormatClass(TextInputFormat.class);
    FileInputFormat.addInputPath(job, new Path("/user/sriksun/test"));
    FileInputFormat.setMaxInputSplitSize(job, 2);
    FileInputFormat.setMinInputSplitSize(job, 2);
    job.setJarByClass(TestJob.class);
    job.submit();
    job.waitForCompletion(true);
    return 0;
  }

  public static class MyMapper extends Mapper<LongWritable, Text,
      NullWritable, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value,
                       Context context) throws IOException, InterruptedException {
      int i = 0;
      while (i++ < context.getConfiguration().getInt("iteration.value", 100)) {
        context.progress();
        Thread.sleep(30000);
        LOG.info("Iteration #" + i + " : Sleeping for 30 seconds");
      }
    }
  }
}
