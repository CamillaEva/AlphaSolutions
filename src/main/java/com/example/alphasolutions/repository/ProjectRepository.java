package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProjectRepository {

    private final ProjectMapper projectMapper;
    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.projectMapper = new ProjectMapper();
    }

    //______________________________________________ASSIGN EMP__________________________________________________________
    public void assignSubprojectToProject(int subprojectID, int projectID) {
        String sql = "INSERT INTO PROJECT_SUBPROJECTS (SUBPROJECTID, PROJECTID) VALUES (?,?)";
        jdbcTemplate.update(sql, subprojectID, projectID);
    }

    public List<Integer> showAssignedEmpProject(int projectID) {
        String sql = "SELECT DISTINCT E.EMPID FROM EMP E JOIN EMP_TASK TE ON E.EMPID = TE.EMPID" +
                " JOIN SUBPROJECT_TASKS ST ON TE.TASKID = ST.TASKID JOIN PROJECT_SUBPROJECTS PS ON ST.SUBPROJECTID = " +
                "PS.SUBPROJECTID WHERE PS.PROJECTID = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, projectID);
    }

    //_______________________________________________CREATE_____________________________________________________________
    public int createProject(Project project) {
        String sql = "INSERT INTO PROJECT (NAME, DESCRIPTION, STARTDATE, ENDDATE) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setDate(3, Date.valueOf(project.getStartDate()));
            ps.setDate(4, Date.valueOf(project.getEndDate()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }


    //_______________________________________________READ_______________________________________________________________
    public List<Project> readAllProjects() {
        String sql = "SELECT P.PROJECTID, P.NAME, P.DESCRIPTION, P.STARTDATE, P.ENDDATE FROM PROJECT P";
        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    public Project readProjectByID(int projectID) {
        String sql = "SELECT P.PROJECTID, P.NAME, P.DESCRIPTION, P.STARTDATE, P.ENDDATE, SP.SUBPROJECTID AS SPID, SP.NAME AS SPNAME, SP.DESCRIPTION AS SPDESCRIPTION, SP.STARTDATE AS SPSTARTDATE, SP.ENDDATE AS SPENDDATE/*, SP.TIMEEST AS SPTIMEEST*/ " +
                "FROM PROJECT P " +
                "LEFT JOIN SUBPROJECT SP ON P.PROJECTID = SP.PROJECTID WHERE P.PROJECTID = ?";
        return projectMapper.projectWithSubProjects(jdbcTemplate.queryForList(sql, projectID)).get(0);
    }

    public int readTotalTimeEstimateForProject(int projectID) {
        String sql = "SELECT COALESCE(SUM(T.TIMEEST), 0) " +
                "FROM TASK T " +
                "JOIN SUBPROJECT S ON T.SUBPROJECTID = S.SUBPROJECTID " +
                "WHERE S.PROJECTID = ?";

        Integer totalTimeEstimate = jdbcTemplate.queryForObject(sql, Integer.class, projectID);
        return totalTimeEstimate != null ? totalTimeEstimate : 0;
    }

    public int readTotalUsedTimeForProject(int projectId) {
        String sql = "SELECT COALESCE(SUM(T.USEDTIME), 0) AS TOTAL_USEDTIME " +
                "FROM TASK T " +
                "JOIN SUBPROJECT_TASKS ST ON T.TASKID = ST.TASKID " +
                "JOIN PROJECT_SUBPROJECTS PS ON ST.SUBPROJECTID = PS.SUBPROJECTID " +
                "WHERE PS.PROJECTID = ?";

        Integer totalUsedTime = jdbcTemplate.queryForObject(sql, Integer.class, projectId);
        return totalUsedTime != null ? totalUsedTime : 0;
    }

    public List<Project> readMyProjects(int empID) {
        String sql = "SELECT DISTINCT P.* FROM PROJECT P JOIN PROJECT_SUBPROJECTS PS ON P.PROJECTID = PS.PROJECTID" +
                " JOIN SUBPROJECT_TASKS ST ON PS.SUBPROJECTID = ST.SUBPROJECTID JOIN EMP_TASK ET ON ST.TASKID = ET.TASKID " +
                "WHERE EMPID = ?";
        return jdbcTemplate.query(sql, new ProjectRowMapper(), empID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateProject(Project project) {
        String sql = "UPDATE project SET NAME = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ? WHERE PROJECTID = ?";
        jdbcTemplate.update(
                sql, project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getProjectID());
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteProject(Project project) {
        for (Subproject s : project.getSubProjects()) {
            List<Integer> taskIDs = jdbcTemplate.query("SELECT TASKID FROM TASK WHERE SUBPROJECTID = ? ",
                    (rs, rowNum) -> rs.getInt("TASKID"),
                    s.getSubProjectID());
            for (Integer taskID : taskIDs) {
                String sql = "DELETE FROM EMP_TASK WHERE TASKID = ?";
                String sql1 = "DELETE FROM SUBPROJECT_TASKS WHERE TASKID = ?";
                String sql2 = "DELETE FROM TASK WHERE TASKID = ?";
                jdbcTemplate.update(sql, taskID);
                jdbcTemplate.update(sql1, taskID);
                jdbcTemplate.update(sql2, taskID);
            }
            String sql3 = "DELETE FROM PROJECT_SUBPROJECTS WHERE SUBPROJECTID = ?";
            String sql4 = "DELETE FROM SUBPROJECT WHERE SUBPROJECTID = ?";

            jdbcTemplate.update(sql3, s.getSubProjectID());
            jdbcTemplate.update(sql4, s.getSubProjectID());
        }
        String sql5 = "DELETE FROM SUBPROJECT WHERE PROJECTID = ?";
        jdbcTemplate.update(sql5, project.getProjectID());

        String sql6 = "DELETE FROM PROJECT WHERE PROJECTID = ?";
        jdbcTemplate.update(sql6, project.getProjectID());
    }

}
