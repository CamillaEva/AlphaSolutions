package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.*;
import com.example.alphasolutions.service.SubprojectService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SubprojectRepository {

    private final SubprojectMapper subprojectMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SubprojectService subprojectService;

    public SubprojectRepository(DataSource dataSource, SubprojectService subprojectService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.subprojectMapper = new SubprojectMapper();
        this.subprojectService = subprojectService;
    }

    //______________________________________________ASSIGN EMP__________________________________________________________
    public void assignTaskToSubproject(int taskID, int subprojectID) {
        String sql = "INSERT INTO SUBPROJECT_TASKS (TASKID, SUBPROJECTID) VALUES (?,?)";
        jdbcTemplate.update(sql, taskID, subprojectID);
    }

    public List<Integer> showAssignedEmpSubproject(int subprojectID) {
        //DISTINCT shows assign emp once not multiple times
        String sql = "SELECT DISTINCT E.EMPID FROM EMP E JOIN EMP_TASK TE ON E.EMPID = TE.EMPID" +
                " JOIN SUBPROJECT_TASKS ST ON TE.TASKID = ST.TASKID WHERE ST.SUBPROJECTID = ?";

        //Integer.class refers to the class object of the integer
        //queryForList expect to return an Integer (empID)
        return jdbcTemplate.queryForList(sql, Integer.class, subprojectID);
    }

    //_______________________________________________CREATE_____________________________________________________________
    public int createSubProject(Subproject subproject) {
        String sql = "INSERT INTO subproject (NAME, DESCRIPTION, STARTDATE, ENDDATE, PROJECTID) VALUES (?,?,?,?,?)";
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, subproject.getName());
            ps.setString(2, subproject.getDescription());
            ps.setDate(3, Date.valueOf(subproject.getStartDate()));
            ps.setDate(4, Date.valueOf(subproject.getEndDate()));
            ps.setInt(5, subproject.getProjectID());
            return ps;
        }, keyholder);

        return keyholder.getKey().intValue();
    }

    //_______________________________________________READ_______________________________________________________________
    public List<Subproject> readMySubprojects(int empID, int projectID) {
        String sql = "SELECT DISTINCT S.SUBPROJECTID, S.PROJECTID, S.NAME, S.DESCRIPTION, S.STARTDATE, S.ENDDATE FROM SUBPROJECT S " +
                "JOIN SUBPROJECT_TASKS ST ON S.SUBPROJECTID = ST.SUBPROJECTID JOIN EMP_TASK ET ON ST.TASKID = ET.TASKID " +
                "WHERE ET.EMPID = ? AND S.PROJECTID = ?";
        return jdbcTemplate.query(sql, new SubprojectRowMapper(), projectID, empID);
    }

    public Subproject readSubprojectById(int subprojectID) {
        String sql = "SELECT SP.SUBPROJECTID, SP.PROJECTID, SP.NAME, SP.DESCRIPTION, SP.STARTDATE, SP.ENDDATE, T.TASKID AS TID, T.NAME AS TNAME, T.DESCRIPTION AS TDESCRIPTION, T.STARTDATE AS TSTARTDATE, T.ENDDATE AS TENDDATE, T.TIMEEST AS TTIMEEST, T.SUBPROJECTID AS TSUBPROJECTID FROM SUBPROJECT SP " +
                "LEFT JOIN TASK T ON SP.SUBPROJECTID = T.SUBPROJECTID WHERE SP.SUBPROJECTID = ?";
        return subprojectMapper.subprojectWithTasks(jdbcTemplate.queryForList(sql, subprojectID)).get(0);
    }

    public int getTimeEstFromTasks(int subprojectID) {
        Subproject subprojectServicerobject = readSubprojectById(subprojectID);
        if (subprojectServicerobject.getTasks() == null) {
            return 0;
        }
        return subprojectServicerobject.getTasks().stream().mapToInt(Task::getTimeEst).sum(); //readSubProjectByID henter et SubProject, som også indeholder en liste af Task-objekter. Derefter bruger vi Java Streams til at summere alle task.getTimeEst().
    }

    public List<Subproject> readSubprojectsByProjectID(int projectID) {
        String sql = "SELECT SUBPROJECTID, PROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE " +
                "FROM SUBPROJECT WHERE PROJECTID = ?";
        return jdbcTemplate.query(sql, new SubprojectRowMapper(), projectID);
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

    public int readTotalUsedTimeForProject(int projectId) {
        String sql = "SELECT COALESCE(SUM(T.USEDTIME), 0) AS TOTAL_USEDTIME " +
                "FROM TASK T " +
                "JOIN SUBPROJECT S ON T.SUBPROJECTID = S.SUBPROJECTID " +
                "WHERE S.PROJECTID = ?";
        Integer totalUsedTime = jdbcTemplate.queryForObject(sql, Integer.class, projectId);
        return totalUsedTime != null ? totalUsedTime : 0;
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateSubproject(Subproject subproject) {
        String sql = "UPDATE SUBPROJECT SET NAME = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ? WHERE SUBPROJECTID = ?";
        jdbcTemplate.update(sql, subproject.getName(), subproject.getDescription(), subproject.getStartDate(), subproject.getEndDate(), subproject.getSubprojectID());

    }

    //_______________________________________________DELETE_____________________________________________________________

    public void deleteSubproject(Subproject subproject) {
        for (Task t : subproject.getTasks()) {
            String sql = "DELETE FROM EMP_TASK WHERE TASKID = ?";
            String sql1 = "DELETE FROM SUBPROJECT_TASKS WHERE TASKID = ?";
            String sql2 = "DELETE FROM TASK WHERE TASKID = ?";
            jdbcTemplate.update(sql, t.getTaskID());
            jdbcTemplate.update(sql1, t.getTaskID());
            jdbcTemplate.update(sql2, t.getTaskID());
        }
        String sql3 = "DELETE FROM PROJECT_SUBPROJECTS WHERE SUBPROJECTID = ?";
        String sql4 = "DELETE FROM SUBPROJECT WHERE SUBPROJECTID = ?";

        jdbcTemplate.update(sql3, subproject.getSubprojectID());
        jdbcTemplate.update(sql4, subproject.getSubprojectID());
    }


}
