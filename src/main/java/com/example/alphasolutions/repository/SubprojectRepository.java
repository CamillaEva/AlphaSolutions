package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SubprojectRepository {

    private SubprojectMapper subProjectMapper;
    private final JdbcTemplate jdbcTemplate;

    public SubprojectRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_PASSWORD")
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.subProjectMapper = new SubprojectMapper();
    }

    //_______________________________________________CREATE_____________________________________________________________
    public int createSubProject(Subproject subProject) {
        String sql = "INSERT INTO subproject (NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, PROJECTID) VALUES (?,?,?,?,?,?)";
        KeyHolder keyholder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, subProject.getName());
            ps.setString(2, subProject.getDescription());
            ps.setDate(3, Date.valueOf(subProject.getStartDate()));
            ps.setDate(4, Date.valueOf(subProject.getEndDate()));
            ps.setInt(5, subProject.getTimeEst());
            ps.setInt(6, subProject.getProjectID());
            return ps;
        }, keyholder);

        return keyholder.getKey().intValue();
    }

    //_______________________________________________READ_______________________________________________________________
    public List<Subproject> readAllSubProjects() {
        String sql = "SELECT SUBPROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM SUBPROJECT";
        return jdbcTemplate.query(sql, new SubprojectRowMapper());
    }

    public Subproject readSubProjectById(int subProjectID) {
        String sql = "SELECT SP.SUBPROJECTID, SP.PROJECTID, SP.NAME, SP.DESCRIPTION, SP.STARTDATE, SP.ENDDATE, SP.TIMEEST, T.TASKID AS TID, T.NAME AS TNAME, T.DESCRIPTION AS TDESCRIPTION, T.STARTDATE AS TSTARTDATE, T.ENDDATE AS TENDDATE, T.TIMEEST AS TTIMEEST, T.SUBPROJECTID AS TSUBPROJECTID FROM SUBPROJECT SP " +
                "LEFT JOIN TASK T ON SP.SUBPROJECTID = T.SUBPROJECTID WHERE SP.SUBPROJECTID = ?";
        return  subProjectMapper.subProjectWithTasks(jdbcTemplate.queryForList(sql, subProjectID)).get(0);
    }

    public int getTimeEstFromTasks(int subProjectID){
        Subproject subProject = readSubProjectById(subProjectID);
        if (subProject.getTasks() == null){
            return 0;
        }
        return subProject.getTasks().stream().mapToInt(Task::getTimeEst).sum(); //readSubProjectByID henter et SubProject, som også indeholder en liste af Task-objekter. Derefter bruger vi Java Streams til at summere alle task.getTimeEst().

    }

    //TODO Only used in service
    public List<Subproject> getSubProjectsByProjectID(int projectID) {
        String sql = "SELECT SUBPROJECTID, PROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST FROM SUBPROJECT WHERE PROJECTID = ?";
        return jdbcTemplate.query(sql, new SubprojectRowMapper(), projectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateSubProject(Subproject subProject) {
        String sql = "UPDATE SUBPROJECT SET NAME = ?, DESCRIPTION = ?, STARTDATE = ?, ENDDATE = ?, TIMEEST = ? WHERE SUBPROJECTID = ?";
        jdbcTemplate.update(sql, subProject.getName(), subProject.getDescription(), subProject.getStartDate(), subProject.getEndDate(), subProject.getTimeEst(), subProject.getSubProjectID());

    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteSubProject(int id) {
        String sql = "DELETE FROM SUBPROJECT WHERE SUBPROJECTID = ?";
        jdbcTemplate.update(sql, id);
    }


}
