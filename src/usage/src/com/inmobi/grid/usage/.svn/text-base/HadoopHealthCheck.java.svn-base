package com.inmobi.grid.usage;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.FSConstants;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * User: srikanth.sundarrajan
 * Date: 9/18/11
 */
public class HadoopHealthCheck extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new HadoopHealthCheck(), args);
    }

    @Override
    public int run(String[] strings) throws Exception {
        DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(getConf());
        DatanodeInfo[] live = fs.getClient().datanodeReport(
                                                     FSConstants.DatanodeReportType.LIVE);
        DatanodeInfo[] dead = fs.getClient().datanodeReport(
                                                     FSConstants.DatanodeReportType.DEAD);

        System.out.println("---------------------------------------------");
        System.out.println("Host                   lastcontact             remaining-GB");
        for (DatanodeInfo dn : live) {
            System.out.println(dn.getHostName() + " " + (System.currentTimeMillis() - dn.getLastUpdate())/ 1000 + " "
                    + dn.getRemaining() / (1024 * 1024 * 1024));
        }

        System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println("DFS Health");
        System.out.println("---------------------------------------------");
        System.out.println("Underreplciated " + fs.getUnderReplicatedBlocksCount());
        System.out.println("Corrupt " + fs.getCorruptBlocksCount());
        System.out.println("Missing blocks: " + fs.getMissingBlocksCount());

        System.out.println("Live Datanodes " + live.length);
        System.out.println("Dead Datanodes " + dead.length);
        fs.close();

        System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println("JT Health");
        System.out.println("---------------------------------------------");
        JobClient job = new JobClient(new JobConf(getConf()));
        ClusterStatus cluster = job.getClusterStatus(true);
        System.out.println("Maps: " + cluster.getMapTasks());
        System.out.println("Reduces: " + cluster.getReduceTasks());
        System.out.println("Max Maps: " + cluster.getMaxMapTasks());
        System.out.println("Max Reduces: " + cluster.getMaxReduceTasks());
        System.out.println("TaskTrackers: " + cluster.getTaskTrackers());
        System.out.println("Active: " + cluster.getActiveTrackerNames().size());
        System.out.println("Blacklisted: " + cluster.getBlacklistedTrackers());
        System.out.println("JT State: " + cluster.getJobTrackerState());
        return 0;
    }
}
