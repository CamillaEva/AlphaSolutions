package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.model.TaskRowMapper;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ConnectionDetails connectionDetails; //TODO: skal den her ikke fjernes??

    public TaskRepository(JdbcTemplate jdbcTemplate, ConnectionDetails connectionDetails) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_PASSWORD")
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.jdbcTemplate = jdbcTemplate;
        this.connectionDetails = connectionDetails;
    }

    //______________________________________________ASSIGN EMP__________________________________________________________
    public void assignEmpToTask(int taskID, int empID){
        String sql = "INSERT INTO EMP_TASK (EMPID, TASKID) VALUES (?,?)";
        jdbcTemplate.update(sql, empID, taskID);
    }

    public List<Integer> showAssignedEmpTask(int taskID){
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
        String sql = "SELECT TASKID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM TASK";
        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    public Task readTaskByID(int taskID) {
        String sql = "SELECT TASKID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, SUBPROJECTID FROM TASK WHERE TASKID = ?";
        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), taskID);
    }

    public List<Task> readMyTasks(int empID) {
        String sql = "SELECT * FROM TASK T " +
                "JOIN EMP_TASK ET ON T.TASKID = ET.TASKID " +
                "WHERE ET.EMPID = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), empID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateTask(Task task) {
        String sql = "UPDATE TASK SET NAME = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ?, TIMEEST = ? WHERE TASKID = ?";
        jdbcTemplate.update(sql, task.getName(), task.getDescription(), task.getStartDate(), task.getEndDate(), task.getTimeEst(), task.getTaskID());
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
