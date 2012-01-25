package com.inmobi.grid.usage.db;

import com.inmobi.grid.usage.JobDetails;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

/**
 * User: srikanth.sundarrajan
 * Date: Mar 18, 2011
 * Time: 3:40:45 PM
 */
public class DBUtil {

    private static DBUtil instance = new DBUtil();

    private DBUtil() {}

    public static DBUtil getInstance() {
        return instance;
    }

    private static final String selectJobSQL = "select count(*) from %SCHEMA%hadoop_jobs where cluster = ? and job_id = ?";
    private static final String removeJobSQL = "delete from %SCHEMA%hadoop_jobs where cluster = ? and job_id = ?";
    private static final String insertJobSQL = "insert into %SCHEMA%hadoop_jobs ( " +
            "cluster, job_id, job_name, job_type, job_minute, num_maps, num_reduces, job_status, job_wait, job_duration, " +
            "total_task_wait, total_task_duration, max_task_wait, map_duration, reduce_duration, bytes_read, bytes_written) " +
            " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String selectPJobSQL = "select count(*) from %SCHEMA%hadoop_parallel_jobs where cluster = ? and minute = ?";
    private static final String removePJobSQL = "delete from %SCHEMA%hadoop_parallel_jobs where cluster = ? and minute = ?";
    private static final String insertPJobSQL = "insert into %SCHEMA%hadoop_parallel_jobs ( " +
            "cluster, minute, num_jobs) values (?, ?, ?)";

    private static final String SCHEMA_PREFIX = "%SCHEMA%";

    private PreparedStatement selectJobQuery;
    private PreparedStatement insertJobQuery;
    private PreparedStatement deleteJobQuery;

    private PreparedStatement selectPJobQuery;
    private PreparedStatement insertPJobQuery;
    private PreparedStatement deletePJobQuery;

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void initializeDriver(Properties appProperties) throws SQLException {
        if (true) return;
        try {
            Class.forName(appProperties.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(2);
        }
        String url = appProperties.getProperty("jdbc.connect.url");
        String user = appProperties.getProperty("jdbc.db.user");
        String password = appProperties.getProperty("jdbc.db.password");
        connection = DriverManager.getConnection(url, user, password);

    }

    public void prepareStatements(Properties appProperties) throws SQLException {
        if (true) return;
        String schema = appProperties.getProperty("jdbc.db.schema.prefix", "");
        selectJobQuery = connection.prepareStatement(selectJobSQL.replaceAll(SCHEMA_PREFIX, schema));
        deleteJobQuery = connection.prepareStatement(removeJobSQL.replaceAll(SCHEMA_PREFIX, schema));
        insertJobQuery = connection.prepareStatement(insertJobSQL.replaceAll(SCHEMA_PREFIX, schema));

        selectPJobQuery = connection.prepareStatement(selectPJobSQL.replaceAll(SCHEMA_PREFIX, schema));
        deletePJobQuery = connection.prepareStatement(removePJobSQL.replaceAll(SCHEMA_PREFIX, schema));
        insertPJobQuery = connection.prepareStatement(insertPJobSQL.replaceAll(SCHEMA_PREFIX, schema));
    }

    public void terminate() throws SQLException {
        if (true) return;
        connection.close();
    }

    public void updateDBWithJobDetails(JobDetails jobDetails) throws SQLException {
        System.out.println(jobDetails);
        if (true) return;
        selectJobQuery.setString(1, jobDetails.getCluster());
        selectJobQuery.setString(2, jobDetails.getJobID());

        deleteJobQuery.setString(1, jobDetails.getCluster());
        deleteJobQuery.setString(2, jobDetails.getJobID());

        ResultSet result = selectJobQuery.executeQuery();
        boolean exists = result.next() && result.getInt(1) > 0;
        if (!exists || deleteJobQuery.executeUpdate() > 0) {
            insertJobQuery.setString(1, jobDetails.getCluster());
            insertJobQuery.setString(2, jobDetails.getJobID());
            insertJobQuery.setString(3, jobDetails.getJobName());
            insertJobQuery.setString(4, jobDetails.getJobType());
            insertJobQuery.setString(5, jobDetails.getJobMinute());
            insertJobQuery.setInt(6, jobDetails.getNumMaps());
            insertJobQuery.setInt(7, jobDetails.getNumReduces());
            insertJobQuery.setString(8, jobDetails.getJobStatus());
            insertJobQuery.setLong(9, jobDetails.getJobWait());
            insertJobQuery.setLong(10, jobDetails.getJobDuration());
            insertJobQuery.setLong(11, jobDetails.getTotalTaskWait());
            insertJobQuery.setLong(12, jobDetails.getTotalTaskDuration());
            insertJobQuery.setLong(13, jobDetails.getMaxTaskWait());
            insertJobQuery.setLong(14, jobDetails.getMapDuration());
            insertJobQuery.setLong(15, jobDetails.getReduceDuration());
            insertJobQuery.setLong(16, jobDetails.getBytesRead());
            insertJobQuery.setLong(17, jobDetails.getBytesWritten());
            exists = (insertJobQuery.executeUpdate() > 0);
        }
        if (!exists) {
            throw new RuntimeException("Unable to insert job for " + jobDetails);
        }
    }

    public void updateDBWithParallelJobInfo(String cluster,
                                                   Map<String, Integer> parallelJobs) throws SQLException {

        for (Map.Entry<String, Integer> pJobEntry : parallelJobs.entrySet()) {
            System.out.println(cluster + "," + pJobEntry.getKey() + "," + pJobEntry.getValue());
            if (true) continue;
            selectPJobQuery.setString(1, cluster);
            selectPJobQuery.setString(2, pJobEntry.getKey());

            deletePJobQuery.setString(1, cluster);
            deletePJobQuery.setString(2, pJobEntry.getKey());

            ResultSet result = selectPJobQuery.executeQuery();
            boolean exists = result.next() && result.getInt(1) > 0;
            if (!exists || deletePJobQuery.executeUpdate() > 0) {
                insertPJobQuery.setString(1, cluster);
                insertPJobQuery.setString(2, pJobEntry.getKey());
                insertPJobQuery.setInt(3, pJobEntry.getValue());
                exists = (insertPJobQuery.executeUpdate() > 0);
            }
            if (!exists) {
                System.out.println("Unable to insert job for " + pJobEntry);
            }
        }
    }

}
