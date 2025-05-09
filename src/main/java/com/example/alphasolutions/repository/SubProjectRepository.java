package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubProjectRepository {

    private SubProjectMapper subProjectMapper;
    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_PASSWORD")
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.subProjectMapper = new SubProjectMapper();
    }


    //________________________________CREATE_________________________________________


    public void createSubProject(SubProject subProject){
        String sql = "INSERT INTO SUBPROJECT(NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, subProject.getName(), subProject.getDescription(), subProject.getStartDate(), subProject.getEndDate(), subProject.getTimeEst());

    }


    public void addSubProject(SubProject subProject){
        String sql = "INSERT INTO subproject (NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, PROJECTID) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, subProject.getName(), subProject.getDescription(), subProject.getStartDate(), subProject.getEndDate(), subProject.getTimeEst(), subProject.getProjectID());
    }


    //________________________________READ____________________________________________


    public List<SubProject> readAllSubProjects(){
        String sql = "SELECT SUBPROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM SUBPROJECT";
        return jdbcTemplate.query(sql, new SubProjectRowMapper());
    }


    public SubProject getSubProjectById(int subProjectID) {
        String sql = "SELECT SP.SUBPROJECTID, SP.NAME, SP.DESCRIPTION, SP.STARTDATE, SP.ENDDATE, SP.TIMEEST, T.TASKID AS TID, T.NAME AS TName, T.DESCRIPTION AS TDESCRIPTION FROM SUBPROJECT SP " +
                "INNER JOIN TASK T ON SP.SUBPROJECTID = T.SUBPROJECTID WHERE SP.SUBPROJECTID = ?";
        return  subProjectMapper.subProjectWithTasks(jdbcTemplate.queryForList(sql, subProjectID)).get(0);
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
