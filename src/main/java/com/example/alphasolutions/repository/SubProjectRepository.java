package com.example.alphasolutions.repository;

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

    public List<SubProject> readAllSubProjects(){
        String sql = "SELECT SUBPROJECTID, NAME, DESCRIPTION, STARTDATE, ENDDATE, TIMEEST, USEDTIME FROM subProject";
        return jdbcTemplate.query(sql, new SubProjectRowMapper());
    }

//    public createSubProject(){
//
//    }
//
//    public updateSubProject(){
//
//    }
//
//    public deleteSubProject(){
//
//    }



}
