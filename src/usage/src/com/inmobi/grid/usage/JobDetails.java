package com.inmobi.grid.usage;

import org.apache.hadoop.hdfs.protocol.QuotaExceededException;

import java.util.Date;
import java.util.List;

/**
 * User: srikanth.sundarrajan
 * Date: Mar 18, 2011
 * Time: 3:45:55 PM
 */
public class JobDetails {
    private static final String QUOTE = "\"";

    private String cluster;
    private String jobID;
    private String jobName;
    private String jobType;
    private String jobMinute;
    private int numMaps;
    private int numReduces;
    private long submitTime;
    private long finishTime;
    private String jobStatus;
    private long jobWait;
    private long jobDuration;
    private long totalTaskWait;
    private long totalTaskDuration;
    private long maxTaskWait;
    private long mapDuration;
    private long reduceDuration;
    private long bytesRead;
    private long bytesWritten;
    private List<TaskStatus> tasks;

    private String slowMapTracker1;
    private String slowMapTracker2;
    private String slowReduceTracker1;
    private String slowReduceTracker2;
    private String slowSpecMapTracker1;
    private String slowSpecMapTracker2;
    private String slowSpecReduceTracker1;
    private String slowSpecReduceTracker2;
    private String fastMapTracker1;
    private String fastMapTracker2;
    private String fastReduceTracker1;
    private String fastReduceTracker2;
    private long fastestMapDataVolumes;
    private long slowestMapDataVolumes;
    private long fastestReduceDataVolumes;
    private long slowestReduceDataVolumes;

    public static class TaskStatus {

        private String taskID;
        private String host;
        private long submitTime;
        private long finishTime;
        private String status;
        private String type;
        private long duration;
        private long bytesRead;
        private long bytesWritten;
        private long mapOutput;
        private long reduceShuffle;

        public String getTaskID() {
            return taskID;
        }

        public void setTaskID(String taskID) {
            this.taskID = taskID;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public long getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(long submitTime) {
            this.submitTime = submitTime;
        }

        public long getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(long finishTime) {
            this.finishTime = finishTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public long getBytesRead() {
            return bytesRead;
        }

        public void setBytesRead(long bytesRead) {
            this.bytesRead = bytesRead;
        }

        public long getBytesWritten() {
            return bytesWritten;
        }

        public void setBytesWritten(long bytesWritten) {
            this.bytesWritten = bytesWritten;
        }

        public long getMapOutput() {
            return mapOutput;
        }

        public void setMapOutput(long mapOutput) {
            this.mapOutput = mapOutput;
        }

        public long getReduceShuffle() {
            return reduceShuffle;
        }

        public void setReduceShuffle(long reduceShuffle) {
            this.reduceShuffle = reduceShuffle;
        }
    }

    public long getFastestReduceDataVolumes() {
        return fastestReduceDataVolumes;
    }

    public void setFastestReduceDataVolumes(long fastestReduceDataVolumes) {
        this.fastestReduceDataVolumes = fastestReduceDataVolumes;
    }

    public long getSlowestReduceDataVolumes() {
        return slowestReduceDataVolumes;
    }

    public void setSlowestReduceDataVolumes(long slowestReduceDataVolumes) {
        this.slowestReduceDataVolumes = slowestReduceDataVolumes;
    }

    public String getSlowReduceTracker1() {
        return slowReduceTracker1;
    }

    public void setSlowReduceTracker1(String slowReduceTracker1) {
        this.slowReduceTracker1 = slowReduceTracker1;
    }

    public String getSlowReduceTracker2() {
        return slowReduceTracker2;
    }

    public void setSlowReduceTracker2(String slowReduceTracker2) {
        this.slowReduceTracker2 = slowReduceTracker2;
    }

    public String getSlowSpecMapTracker1() {
        return slowSpecMapTracker1;
    }

    public void setSlowSpecMapTracker1(String slowSpecMapTracker1) {
        this.slowSpecMapTracker1 = slowSpecMapTracker1;
    }

    public String getSlowSpecMapTracker2() {
        return slowSpecMapTracker2;
    }

    public void setSlowSpecMapTracker2(String slowSpecMapTracker2) {
        this.slowSpecMapTracker2 = slowSpecMapTracker2;
    }

    public String getSlowSpecReduceTracker1() {
        return slowSpecReduceTracker1;
    }

    public void setSlowSpecReduceTracker1(String slowSpecReduceTracker1) {
        this.slowSpecReduceTracker1 = slowSpecReduceTracker1;
    }

    public String getSlowSpecReduceTracker2() {
        return slowSpecReduceTracker2;
    }

    public void setSlowSpecReduceTracker2(String slowSpecReduceTracker2) {
        this.slowSpecReduceTracker2 = slowSpecReduceTracker2;
    }

    public String getFastReduceTracker1() {
        return fastReduceTracker1;
    }

    public void setFastReduceTracker1(String fastReduceTracker1) {
        this.fastReduceTracker1 = fastReduceTracker1;
    }

    public String getFastReduceTracker2() {
        return fastReduceTracker2;
    }

    public void setFastReduceTracker2(String fastReduceTracker2) {
        this.fastReduceTracker2 = fastReduceTracker2;
    }

    public String getSlowMapTracker1() {
        return slowMapTracker1;
    }

    public void setSlowMapTracker1(String slowMapTracker1) {
        this.slowMapTracker1 = slowMapTracker1;
    }

    public String getSlowMapTracker2() {
        return slowMapTracker2;
    }

    public void setSlowMapTracker2(String slowMapTracker2) {
        this.slowMapTracker2 = slowMapTracker2;
    }

    public String getSpecTracker1() {
        return slowSpecMapTracker1;
    }

    public void setSpecTracker1(String specTracker1) {
        this.slowSpecMapTracker1 = specTracker1;
    }

    public String getSpecTracker2() {
        return slowSpecMapTracker2;
    }

    public void setSpecTracker2(String specTracker2) {
        this.slowSpecMapTracker2 = specTracker2;
    }

    public String getFastMapTracker1() {
        return fastMapTracker1;
    }

    public void setFastMapTracker1(String fastMapTracker1) {
        this.fastMapTracker1 = fastMapTracker1;
    }

    public String getFastMapTracker2() {
        return fastMapTracker2;
    }

    public void setFastMapTracker2(String fastMapTracker2) {
        this.fastMapTracker2 = fastMapTracker2;
    }

    public long getFastestMapDataVolumes() {
        return fastestMapDataVolumes;
    }

    public void setFastestMapDataVolumes(long fastestMapDataVolumes) {
        this.fastestMapDataVolumes = fastestMapDataVolumes;
    }

    public long getSlowestMapDataVolumes() {
        return slowestMapDataVolumes;
    }

    public void setSlowestMapDataVolumes(long slowestMapDataVolumes) {
        this.slowestMapDataVolumes = slowestMapDataVolumes;
    }

    public List<TaskStatus> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskStatus> tasks) {
        this.tasks = tasks;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobMinute() {
        return jobMinute;
    }

    public void setJobMinute(String jobMinute) {
        this.jobMinute = jobMinute;
    }

    public int getNumMaps() {
        return numMaps;
    }

    public void setNumMaps(int numMaps) {
        this.numMaps = numMaps;
    }

    public int getNumReduces() {
        return numReduces;
    }

    public void setNumReduces(int numReduces) {
        this.numReduces = numReduces;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public long getJobWait() {
        return jobWait;
    }

    public void setJobWait(long jobWait) {
        this.jobWait = jobWait;
    }

    public long getJobDuration() {
        return jobDuration;
    }

    public void setJobDuration(long jobDuration) {
        this.jobDuration = jobDuration;
    }

    public long getTotalTaskWait() {
        return totalTaskWait;
    }

    public void setTotalTaskWait(long totalTaskWait) {
        this.totalTaskWait = totalTaskWait;
    }

    public long getTotalTaskDuration() {
        return totalTaskDuration;
    }

    public void setTotalTaskDuration(long totalTaskDuration) {
        this.totalTaskDuration = totalTaskDuration;
    }

    public long getMaxTaskWait() {
        return maxTaskWait;
    }

    public void setMaxTaskWait(long maxTaskWait) {
        this.maxTaskWait = maxTaskWait;
    }

    public long getMapDuration() {
        return mapDuration;
    }

    public void setMapDuration(long mapDuration) {
        this.mapDuration = mapDuration;
    }

    public long getReduceDuration() {
        return reduceDuration;
    }

    public void setReduceDuration(long reduceDuration) {
        this.reduceDuration = reduceDuration;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }

    @Override
    public String toString() {
/*
        StringBuffer job = new StringBuffer(QUOTE + cluster + QUOTE + ", " + QUOTE + jobID + QUOTE + ", " +
                QUOTE + jobName + QUOTE + ", " + QUOTE + jobType + QUOTE + ", " +
                QUOTE + jobMinute + QUOTE + ", " + numMaps + ", " + numReduces + ", " +
                QUOTE + jobStatus + QUOTE + ", " + jobWait + ", " + jobDuration + ", " + totalTaskWait + ", " +
                totalTaskDuration + ", " + maxTaskWait + ", " + mapDuration + ", " +
                reduceDuration + ", " + bytesRead + ", " + bytesWritten + ", " + submitTime + ", " + finishTime);
*/
        StringBuffer job = new StringBuffer();
/*
        for (TaskStatus task : tasks) {
            job.append("\nTASK:").append(QUOTE).append(jobID).append(QUOTE).append(", ").
                    append(QUOTE).append(task.taskID).append(QUOTE).append(", ").
                    append(QUOTE).append(task.host_new).append(QUOTE).append(", ").
                    append(QUOTE).append(task.type).append(QUOTE).
                    append(",").append(QUOTE).append(task.status).append(QUOTE).append(", ").
                    append(task.duration).append(", ").append(new Date(task.submitTime)).append(", ").
                    append(new Date(task.finishTime)).append(", ").append(task.bytesRead).append(", ").
                            append(task.bytesWritten).append(", ").append(task.mapOutput).append(", ").append(task.reduceShuffle);
        }
*/
        job.append(QUOTE).append(jobID).append(QUOTE).append(", ").append(QUOTE).append(jobName).append(QUOTE).append(", ").
                append(numMaps).append(", ").append(numReduces).append(", ").append(QUOTE).append(jobStatus).append(QUOTE).
                append(", ").append(jobDuration).append(", ").
                append(QUOTE).append(slowMapTracker1).append(QUOTE).append(", ").
                append(QUOTE).append(slowMapTracker2).append(QUOTE).append(", ").
                append(QUOTE).append(slowReduceTracker1).append(QUOTE).append(", ").
                append(QUOTE).append(slowReduceTracker2).append(QUOTE).append(", ").
                append(QUOTE).append(slowSpecMapTracker1).append(QUOTE).append(", ").
                append(QUOTE).append(slowSpecMapTracker2).append(QUOTE).append(", ").
                append(QUOTE).append(slowSpecReduceTracker1).append(QUOTE).append(", ").
                append(QUOTE).append(slowSpecReduceTracker2).append(QUOTE).append(", ").
                append(QUOTE).append(fastMapTracker1).append(QUOTE).append(", ").
                append(QUOTE).append(fastMapTracker2).append(QUOTE).append(", ").
                append(QUOTE).append(fastReduceTracker1).append(QUOTE).append(", ").
                append(QUOTE).append(fastReduceTracker2).append(QUOTE).append(", ").
                append(slowestMapDataVolumes).append(", ").append(slowestReduceDataVolumes).append(", ").
                append(fastestMapDataVolumes).append(", ").append(fastestReduceDataVolumes);
        return job.toString();
    }
}
