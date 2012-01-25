package com.inmobi.grid.usage;

import com.inmobi.grid.usage.db.DBUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.DefaultJobHistoryParser;
import org.apache.hadoop.mapred.JobHistory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackUsage {

    /**
     * Application properties such as jdbc connection string, user, password, driver etc
     */
    private static final Properties appProperties = new Properties();

    /**
     * Date format to the precision of a minute. This is the submit minute of the job
     */
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm");

    /**
     * List of known job types
     * TODO:// Too bad to filter by job types. Potentially we ought to be managing independent
     * queues for different cost centers/bu and have accounting done by this. This is merely a
     * stop gap arrangement
     */
    private static final Pattern jobTypes= Pattern.compile("(CTRAggregator)|(JOBAPPTRACKERDLY)|(JOBCLICKSDEMOGHRLY)|" +
            "(JOBCLICKSHRLY)|(JOBREFTAGHRLY)|(PigLatin:'TagUniverse)|(hourlyjob)|(Network)|(yoda)|(PigLatin:)");

    private static final Calendar yesterday = Calendar.getInstance();

    public static final String APP_PROPERTIES_KEY = "app.properties";
    private static final String DEFAULT_APP_PROPERTY_FILE = "usage.properties";

    /**
     * JobDetails entity class that matches the table schema
     */
    private FileSystem historyFS;
    private FileSystem clusterFS;

    private static class HistoryFilter implements PathFilter {

        private final FileSystem fs;
        private final boolean all;

        public HistoryFilter(FileSystem fs, boolean all) {
            this.fs = fs;
            this.all = all;
        }

        @Override
        public boolean accept(Path path) {
            try {
                if (!path.getName().endsWith("xml")) {
                    if (all) return true;
                    Calendar fileTime = Calendar.getInstance();
                    fileTime.setTime(new Date(fs.getFileStatus(path).getModificationTime()));
                    resetTimePart(fileTime);
                    if (fileTime.getTime().equals(yesterday.getTime())) return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    private static void resetTimePart(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
    }

    public static void main(String args[]) throws Exception {
        if (args.length < 1 || args.length > 2) {
            System.err.println("Invalid arguments");
            System.err.println("Usage: TrackUsage <history_dir>");
            System.exit(1);
        }

        boolean all = args.length == 2 && args[1].equals("all");

        loadAppProperties();

        DBUtil.getInstance().initializeDriver(appProperties);
        DBUtil.getInstance().prepareStatements(appProperties);

        yesterday.add(Calendar.DATE, -1);
        resetTimePart(yesterday);

        TrackUsage processor = new TrackUsage();
        processor.gatherStats(args[0], all);
        
        DBUtil.getInstance().terminate();
    }

    public void gatherStats(String historyPath, boolean all) throws Exception {
        historyFS = new Path(historyPath).getFileSystem(new Configuration());
        clusterFS = FileSystem.get(new Configuration());
        PathFilter filter = new HistoryFilter(historyFS, all);
        FileStatus[] files = historyFS.globStatus(new Path(historyPath + "/*"), filter);

        for (FileStatus file : files) {
            try {
                JobDetails jobDetails = readHistory(file.getPath().makeQualified(historyFS));
                DBUtil.getInstance().updateDBWithJobDetails(jobDetails);
                //computeParallelJobs(jobDetails);
            } catch (Exception e) {
                System.err.println("Unable to process " + file.getPath().toString());
                e.printStackTrace();
            }
        }
        String fsUri = clusterFS.getUri().toString();
        String cluster = fsUri.substring(0,(fsUri.length() > 50 ? 50 : fsUri.length()));

        //DBUtil.getInstance().updateDBWithParallelJobInfo(cluster, parallelJobs);
        //System.out.println("Processed " + files.length + " history files");
    }

    private static void loadAppProperties() throws IOException {
        String propFile = System.getProperty(APP_PROPERTIES_KEY, DEFAULT_APP_PROPERTY_FILE);
        InputStream stream = TrackUsage.class.getClassLoader().getResourceAsStream(propFile);
        try {
            appProperties.load(stream);
        } finally {
            stream.close();
        }
    }

    private static Map<String, Integer> parallelJobs = new HashMap<String, Integer>();

    public JobDetails readHistory(Path historyFile) throws Exception {
        String jobID = "";
        JobHistory.JobInfo jobInfo = new JobHistory.JobInfo(jobID);
        DefaultJobHistoryParser.parseJobTasks(historyFile.toString(), jobInfo, historyFS);
        Map<JobHistory.Keys,String> values = jobInfo.getValues();
        String jobName = values.get(JobHistory.Keys.JOBNAME);
        jobID = values.get(JobHistory.Keys.JOBID);
        long launch = Long.parseLong(values.get(JobHistory.Keys.LAUNCH_TIME));
        long submit = Long.parseLong(values.get(JobHistory.Keys.SUBMIT_TIME));
        long finish = Long.parseLong(values.get(JobHistory.Keys.FINISH_TIME));

        int maps = Integer.parseInt(values.get(JobHistory.Keys.TOTAL_MAPS));
        int reduce = Integer.parseInt(values.get(JobHistory.Keys.TOTAL_REDUCES));
        String status = values.get(JobHistory.Keys.JOB_STATUS);
        String counters = values.get(JobHistory.Keys.COUNTERS);
        Counters readCounters = null;
        if (counters != null) {
            readCounters = Counters.fromEscapedCompactString(counters);
        }

        Matcher jobTypeMatcher = jobTypes.matcher(jobName);
        String jobType = jobTypeMatcher.find() ? jobTypeMatcher.group() : "OTHER";

        long queueWait = (launch - submit) / 1000;
        long jobExec = (finish - launch) / 1000;

        long totalTaskWait = 0;
        long maxTaskWait = 0;
        long totalTaskDuration = 0;
        long maxTaskDuration = 0;
        long mapSeconds = 0;
        long reduceSeconds = 0;

        List<JobDetails.TaskStatus> tasks = new ArrayList<JobDetails.TaskStatus>();
        List<JobDetails.TaskStatus> killedTasks = new ArrayList<JobDetails.TaskStatus>();
        for (Map.Entry<String, JobHistory.Task> entry : jobInfo.getAllTasks().entrySet()) {
            Map<JobHistory.Keys, String> tVals = entry.getValue().getValues();
            for (Map.Entry<String, JobHistory.TaskAttempt> attemptEntry : entry.getValue().getTaskAttempts().entrySet()) {
                JobHistory.TaskAttempt attempt = attemptEntry.getValue();
                JobDetails.TaskStatus task = new JobDetails.TaskStatus();
                task.setHost(attempt.get(JobHistory.Keys.HOSTNAME));
                task.setTaskID(attempt.get(JobHistory.Keys.TASK_ATTEMPT_ID));
                long st = Long.parseLong(attempt.get(JobHistory.Keys.START_TIME));
                long fn = Long.parseLong(attempt.get(JobHistory.Keys.FINISH_TIME));
                task.setStatus(attempt.get(JobHistory.Keys.TASK_STATUS));
                task.setType(attempt.get(JobHistory.Keys.TASK_TYPE));
                task.setSubmitTime(st);
                task.setFinishTime(fn);
                task.setDuration(fn - st);
                Counters taskCounters = null;
                counters = attempt.get(JobHistory.Keys.COUNTERS);
                if (counters != null) {
                    taskCounters = Counters.fromEscapedCompactString(counters);
                    if (taskCounters!= null) {
                        try {
                            task.setBytesRead(taskCounters.getGroup("FileSystemCounters").getCounter("HDFS_BYTES_READ"));
                            task.setBytesWritten(taskCounters.getGroup("FileSystemCounters").getCounter("HDFS_BYTES_WRITTEN"));
                            task.setMapOutput(taskCounters.getGroup("org.apache.hadoop.mapred.Task$Counter").getCounterForName("MAP_OUTPUT_BYTES").getCounter());
                            task.setReduceShuffle(taskCounters.getGroup("org.apache.hadoop.mapred.Task$Counter").getCounterForName("REDUCE_SHUFFLE_BYTES").getCounter());
                        } catch (Exception ignore) { }
                    }
                }
                tasks.add(task);
                if (task.getStatus().equals("KILLED")) {
                    killedTasks.add(task);
                }
            }

            //This task never got scheduled/finished. skip it
            if (tVals.get(JobHistory.Keys.FINISH_TIME) == null) continue;

            long taskStart = Long.parseLong(tVals.get(JobHistory.Keys.START_TIME));
            long taskFinish = Long.parseLong(tVals.get(JobHistory.Keys.FINISH_TIME));
            String taskType = tVals.get(JobHistory.Keys.TASK_TYPE);

            totalTaskWait += ((taskStart - submit) / 1000);
            if (maxTaskWait < ((taskStart - submit) / 1000)) {
                maxTaskWait = ((taskStart - submit) / 1000);
            }

            totalTaskDuration += ((taskFinish - taskStart) / 1000);
            if (maxTaskDuration < ((taskFinish - taskStart) / 1000)) {
                maxTaskDuration = ((taskFinish - taskStart) / 1000);
            }

            if (taskType.equals("MAP")) {
                mapSeconds += ((taskFinish - taskStart) / 1000);
            } else if (taskType.equals("REDUCE")) {
                reduceSeconds += ((taskFinish - taskStart) / 1000);
            }
        }

        Comparator<JobDetails.TaskStatus> durationComparator = new Comparator<JobDetails.TaskStatus>() {
            @Override
            public int compare(JobDetails.TaskStatus o1, JobDetails.TaskStatus o2) {
                return (int)(o1.getDuration() - o2.getDuration());
            }
        };

        Collections.sort(tasks, durationComparator);
        JobDetails jobDetails = new JobDetails();

        for (int index = tasks.size() -1 ; index >= 0; index--) {
            if (index < 0) break;
            if (tasks.get(index).getStatus().equals("KILLED")) {
                if (tasks.get(index).getType().equals("MAP")) {
                    if (jobDetails.getSlowSpecMapTracker1() == null)  {
                        jobDetails.setSlowSpecMapTracker1(tasks.get(index).getHost());
                    } else if (jobDetails.getSlowSpecMapTracker2() == null)  {
                        jobDetails.setSlowSpecMapTracker2(tasks.get(index).getHost());
                    }
                } else if (tasks.get(index).getType().equals("REDUCE")) {
                    if (jobDetails.getSlowSpecReduceTracker1() == null)  {
                        jobDetails.setSlowSpecReduceTracker1(tasks.get(index).getHost());
                    } else if (jobDetails.getSlowSpecReduceTracker2() == null)  {
                        jobDetails.setSlowSpecReduceTracker2(tasks.get(index).getHost());
                    }
                }
            } else if (tasks.get(index).getStatus().equals("SUCCESS")) {
                if (tasks.get(index).getType().equals("MAP")) {
                    if (jobDetails.getSlowMapTracker1() == null)  {
                        jobDetails.setSlowMapTracker1(tasks.get(index).getHost());
                    } else if (jobDetails.getSlowMapTracker2() == null)  {
                        jobDetails.setSlowMapTracker2(tasks.get(index).getHost());
                    }
                    if (jobDetails.getSlowestMapDataVolumes() == 0) {
                        jobDetails.setSlowestMapDataVolumes(tasks.get(index).getBytesRead());
                    }
                } else if (tasks.get(index).getType().equals("REDUCE")) {
                    if (jobDetails.getSlowReduceTracker1() == null)  {
                        jobDetails.setSlowReduceTracker1(tasks.get(index).getHost());
                    } else if (jobDetails.getSlowReduceTracker2() == null)  {
                        jobDetails.setSlowReduceTracker2(tasks.get(index).getHost());
                    }
                    if (jobDetails.getSlowestReduceDataVolumes() == 0) {
                        jobDetails.setSlowestReduceDataVolumes(tasks.get(index).getReduceShuffle());
                    }
                }
            }
        }
        
        for (int index = 0 ; index < tasks.size(); index++) {
            if (tasks.get(index).getStatus().equals("SUCCESS")) {
                if (tasks.get(index).getType().equals("MAP")) {
                    if (jobDetails.getFastMapTracker1() == null)  {
                        jobDetails.setFastMapTracker1(tasks.get(index).getHost());
                    } else if (jobDetails.getFastMapTracker2() == null)  {
                        jobDetails.setFastMapTracker2(tasks.get(index).getHost());
                    }
                    if (jobDetails.getFastestMapDataVolumes() == 0) {
                        jobDetails.setFastestMapDataVolumes(tasks.get(index).getBytesRead());
                    }
                } else if (tasks.get(index).getType().equals("REDUCE")) {
                    if (jobDetails.getFastReduceTracker1() == null)  {
                        jobDetails.setFastReduceTracker1(tasks.get(index).getHost());
                    } else if (jobDetails.getFastReduceTracker2() == null)  {
                        jobDetails.setFastReduceTracker2(tasks.get(index).getHost());
                    }
                    if (jobDetails.getFastestReduceDataVolumes() == 0) {
                        jobDetails.setFastestReduceDataVolumes(tasks.get(index).getReduceShuffle());
                    }
                }
            }
        }

        jobDetails.setTasks(tasks);
        String fsUri = clusterFS.getUri().toString();
        jobDetails.setCluster(fsUri.substring(0,(fsUri.length() > 50 ? 50 : fsUri.length())));
        jobDetails.setJobID(jobID);
        jobDetails.setJobName(jobName);
        jobDetails.setJobType(jobType);
        jobDetails.setJobMinute(fmt.format(new Date(submit)));
        jobDetails.setJobStatus(status);
        jobDetails.setNumMaps(maps);
        jobDetails.setNumReduces(reduce);
        jobDetails.setJobWait(queueWait);
        jobDetails.setJobDuration(jobExec);
        jobDetails.setTotalTaskWait(totalTaskWait);
        jobDetails.setTotalTaskDuration(totalTaskDuration);
        jobDetails.setMaxTaskWait(maxTaskWait);
        jobDetails.setMapDuration(mapSeconds);
        jobDetails.setReduceDuration(reduceSeconds);
        if (readCounters != null) {
            jobDetails.setBytesRead(readCounters.getGroup("FileSystemCounters").getCounter("HDFS_BYTES_READ"));
            jobDetails.setBytesWritten(readCounters.getGroup("FileSystemCounters").getCounter("HDFS_BYTES_WRITTEN"));
        }
        jobDetails.setSubmitTime(submit);
        jobDetails.setFinishTime(finish);

        return jobDetails;
    }

    private static void computeParallelJobs(JobDetails jobDetails) {
        Calendar submitTime = Calendar.getInstance();
        submitTime.setTime(new Date(jobDetails.getSubmitTime()));

        Calendar finishTime = Calendar.getInstance();
        finishTime.setTime(new Date(jobDetails.getFinishTime()));

        while (submitTime.before(finishTime)) {
            String key = fmt.format(submitTime.getTime());
            if (parallelJobs.containsKey(key)) {
                parallelJobs.put(key, parallelJobs.get(key) + 1);
            } else {
                parallelJobs.put(key, 1);
            }
            submitTime.add(Calendar.MINUTE, 1);
        }
    }
}
