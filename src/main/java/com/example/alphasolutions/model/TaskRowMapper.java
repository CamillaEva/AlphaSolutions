package com.example.alphasolutions.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Task(
                rs.getInt("TASKID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("STARTDATE").toLocalDate(),
                rs.getDate("ENDDATE").toLocalDate(),
                rs.getInt("TIMEEST"),
                rs.getInt("SUBPROJECTID"),
                rs.getInt("USEDTIME")
        );
    }
}

