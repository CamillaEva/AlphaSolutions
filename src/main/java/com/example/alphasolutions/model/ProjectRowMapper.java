package com.example.alphasolutions.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectRowMapper implements RowMapper<Project> {
    @Override
    public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Project(
                rs.getInt("PROJECTID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("STARTDATE").toLocalDate(),
                rs.getDate("ENDDATE").toLocalDate(),
                rs.getInt("TIMEEST"),
                rs.getInt("USEDTIME")
        );
    }
}
