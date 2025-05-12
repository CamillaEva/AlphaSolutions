package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.ProjectMapper;
import com.example.alphasolutions.model.ProjectRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProjectRepository {

    private ProjectMapper projectMapper;
    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_PASSWORD")
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.projectMapper = new ProjectMapper();
    }

    //_______________________________________________CREATE_____________________________________________________________
    public int createProject(Project project) {
        String sql = "INSERT INTO PROJECT (NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setDate(3, Date.valueOf(project.getStartDate()));
            ps.setDate(4, Date.valueOf(project.getEndDate()));
            ps.setInt(5, project.getTimeEst());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }


    //_______________________________________________READ_______________________________________________________________
    public List<Project> readAllProjects() {
        String sql = "SELECT P.PROJECTID, P.NAME, P.DESCRIPTION, P.STARTDATE, P.ENDDATE, P.TIMEEST FROM PROJECT P ";
        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    public Project readProjectByID(int projectID){
        String sql = "SELECT P.PROJECTID, P.NAME, P.DESCRIPTION, P.STARTDATE, P.ENDDATE, P.TIMEEST, SP.SUBPROJECTID AS SPID, SP.NAME AS SPNAME, SP.DESCRIPTION AS SPDESCRIPTION, SP.STARTDATE AS SPSTARTDATE, SP.ENDDATE AS SPENDDATE, SP.TIMEEST AS SPTIMEEST " +
                "FROM PROJECT P " +
                "LEFT JOIN SUBPROJECT SP ON P.PROJECTID = SP.PROJECTID WHERE P.PROJECTID = ?";
        return projectMapper.ProjectWithSubProjects(jdbcTemplate.queryForList(sql, projectID)).get(0);
    }


    //_______________________________________________UPDATE_____________________________________________________________
    public void updateProject(Project project) {
        String sql = "UPDATE project SET NAME = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ?, TIMEEST = ? WHERE PROJECTID = ?";
        jdbcTemplate.update(
                sql, project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate(),
                project.getTimeEst(),
                project.getProjectID());
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteProject(Project project) {
        String deleteSql = "DELETE FROM SUBPROJECT WHERE PROJECTID = ?";
        jdbcTemplate.update(deleteSql, project.getProjectID());

        String sql = "DELETE FROM PROJECT WHERE PROJECTID = ?";
        jdbcTemplate.update(sql, project.getProjectID());
    }

}
