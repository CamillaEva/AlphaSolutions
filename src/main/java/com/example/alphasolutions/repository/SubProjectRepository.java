package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.ProjectRowMapper;
import com.example.alphasolutions.model.SubProject;
import com.example.alphasolutions.model.SubProjectRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_PASSWORD")
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    //________________________________CREATE_________________________________________


    public void createSubProject(SubProject subProject){
        String sql = "INSERT INTO SUBPROJECT(NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, subProject.getName(), subProject.getDescription(), subProject.getStartDate(), subProject.getEndDate(), subProject.getTimeEst());

    }

    //________________________________READ____________________________________________


    public List<SubProject> readAllSubProjects(){
        String sql = "SELECT SUBPROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM SUBPROJECT";
        return jdbcTemplate.query(sql, new SubProjectRowMapper());
    }


    public SubProject getSubProjectById(int subProjectID) {
        String sql = "SELECT SUBPROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM SUBPROJECT WHERE SUBPROJECTID = ?";
        return jdbcTemplate.queryForObject(sql, new SubProjectRowMapper(), subProjectID);
    }


    public List<SubProject> getSubProjectsByProjectID(int projectID){
        String sql = "SELECT SUBPROJECTID, PROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM SUBPROJECT WHERE PROJECTID = ?";
        return jdbcTemplate.query(sql, new SubProjectRowMapper(), projectID);
    }


    //__________________________________UPDATE_______________________________________


    public void updateSubProject(SubProject subProject){
        String sql = "UPDATE SUBPROJECT SET NAME = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ?, TIMEEST = ? WHERE SUBPROJECTID = ?";
        jdbcTemplate.update(sql, subProject.getName(), subProject.getDescription(), subProject.getStartDate(), subProject.getEndDate(), subProject.getTimeEst(), subProject.getSubProjectID());

    }

    //_________________________________DELETE__________________________________________

    public void deleteSubProject(int id){
        String sql = "DELETE FROM SUBPROJECT WHERE SUBPROJECTID = ?";
        jdbcTemplate.update(sql, id);
    }





}
