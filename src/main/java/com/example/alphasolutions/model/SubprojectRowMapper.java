package com.example.alphasolutions.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubprojectRowMapper implements RowMapper<Subproject> {
    @Override

    public Subproject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Subproject(
                rs.getInt("SUBPROJECTID"),
                rs.getInt("PROJECTID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("STARTDATE").toLocalDate(),
                rs.getDate("ENDDATE").toLocalDate()
        );
    }

}
