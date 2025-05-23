package com.example.alphasolutions.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubprojectRowMapper implements RowMapper<SubProject> {
    @Override

    public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SubProject(
                rs.getInt("SUBPROJECTID"),
                rs.getInt("PROJECTID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("STARTDATE").toLocalDate(),
                rs.getDate("ENDDATE").toLocalDate()
        );
    }

}
