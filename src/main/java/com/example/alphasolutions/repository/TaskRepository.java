package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.model.TaskRowMapper;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbcTemplate;
    //private DataSource dataSource;

    public TaskRepository(DataSource dataSource) {
        //this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //______________________________________________ASSIGN EMP__________________________________________________________
    public void assignEmpToTask(int taskID, int empID) {
        String sql = "INSERT INTO EMP_TASK (EMPID, TASKID) VALUES (?,?)";
        jdbcTemplate.update(sql, empID, taskID);
    }

    public List<Integer> showAssignedEmpTask(int taskID) {
        String sql = "SELECT DISTINCT EMPID FROM EMP_TASK WHERE TASKID = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, taskID);
    }


    //_______________________________________________CREATE_____________________________________________________________
    public int createTask(Task task) {
        String sql = "INSERT INTO TASK (NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, SUBPROJECTID) VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getName());
            ps.setString(2, task.getDescription());
            ps.setDate(3, Date.valueOf(task.getStartDate()));
            ps.setDate(4, Date.valueOf(task.getEndDate()));
            ps.setInt(5, task.getTimeEst());
            ps.setInt(6, task.getSubProjectID());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    //_______________________________________________READ_______________________________________________________________
    public List<Task> readAllTask() {
        String sql = "SELECT TASKID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, SUBPROJECTID, USEDTIME FROM TASK";
        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    public Task readTaskByID(int taskID) {
        String sql = "SELECT TASKID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, SUBPROJECTID, USEDTIME FROM TASK WHERE TASKID = ?";
        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), taskID);
    }

    public List<Task> readMyTasks(int empID, int subprojectID) {
        String sql = "SELECT * FROM TASK T " +
                "JOIN EMP_TASK ET ON T.TASKID = ET.TASKID " +
                "WHERE ET.EMPID = ? AND T.SUBPROJECTID = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), empID, subprojectID);
    }

    public int readTotalTimeEstimateForProject(int projectID) {
        String sql = "SELECT COALESCE(SUM(T.TIMEEST), 0) AS TOTAL_ESTIMATE " +
                "FROM TASK T " +
                "JOIN SUBPROJECT_TASKS ST ON T.TASKID = ST.TASKID " +
                "JOIN PROJECT_SUBPROJECTS PS ON ST.SUBPROJECTID = PS.SUBPROJECTID " +
                "WHERE PS.PROJECTID = ?";
        Integer totalTimeEstimate = jdbcTemplate.queryForObject(sql, Integer.class, projectID);
        return totalTimeEstimate != null ? totalTimeEstimate : 0;
    }

    public int readTotalUsedTimeForProject(int projectID) {
        String sql = "SELECT COALESCE(SUM(T.USEDTIME), 0) AS TOTAL_USEDTIME " +
                "FROM TASK T " +
                "JOIN SUBPROJECT_TASKS ST ON T.TASKID = ST.TASKID " +
                "JOIN PROJECT_SUBPROJECTS PS ON ST.SUBPROJECTID = PS.SUBPROJECTID " +
                "WHERE PS.PROJECTID = ?";

        Integer totalUsedTime = jdbcTemplate.queryForObject(sql, Integer.class, projectID);
        return totalUsedTime != null ? totalUsedTime : 0;
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateTask(Task task) {
        String sql = "UPDATE TASK SET NAME = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ?, TIMEEST = ? WHERE TASKID = ?";
        jdbcTemplate.update(sql, task.getName(), task.getDescription(), task.getStartDate(), task.getEndDate(), task.getTimeEst(), task.getTaskID());
    }

    public void updateUsedTime(Task task) {
        String sql = "UPDATE TASK SET USEDTIME = USEDTIME + ? WHERE TASKID = ?";
        jdbcTemplate.update(sql, task.getUsedTime(), task.getTaskID());
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteTask(Task task) {
        String sql = "DELETE FROM EMP_TASK WHERE TASKID = ?";
        String sql1 = "DELETE FROM SUBPROJECT_TASKS WHERE TASKID = ?";
        String sql2 = "DELETE FROM TASK WHERE TASKID = ?";
        jdbcTemplate.update(sql, task.getTaskID());
        jdbcTemplate.update(sql1, task.getTaskID());
        jdbcTemplate.update(sql2, task.getTaskID());
    }

}
