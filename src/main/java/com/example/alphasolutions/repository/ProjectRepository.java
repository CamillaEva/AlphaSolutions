package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Project;
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

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL" ),
                System.getenv("DB_USERNAME" ),
                System.getenv("DB_PASSWORD" )
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver" );
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    //_______________________________________________CREATE METHOD______________________________________________________
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


    //_______________________________________________READ METHOD________________________________________________________
    public List<Project> readAllProjects() {
        String sql = "SELECT PROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, USEDTIME FROM project";
        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    public Project getProjectByID(int projectID){
        String sql = "SELECT PROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM PROJECT WHERE PROJECTID = ?";
        return jdbcTemplate.queryForObject(sql, new ProjectRowMapper(), projectID);
    }




    //______________________________________________UPDATE METHOD_______________________________________________________
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

    //_______________________________________________DELETE METHOD______________________________________________________
    public void deleteProject(Project project){
        String deleteSql = "DELETE FROM SUBPROJECT WHERE PROJECTID = ?";
        jdbcTemplate.update(deleteSql, project.getProjectID());

        String sql = "DELETE FROM PROJECT WHERE PROJECTID = ?";
        jdbcTemplate.update(sql, project.getProjectID());
    }




}
