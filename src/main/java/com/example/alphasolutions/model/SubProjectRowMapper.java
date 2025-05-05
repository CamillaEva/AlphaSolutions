package com.example.alphasolutions.model;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SubProjectRowMapper implements RowMapper<SubProject> {
    @Override

    //lav om til den anden rowmapper måde at skrive det på

    public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
        int SubProjectID = rs.getInt("SUBPROJECTID");
        int TaskID = rs.getInt("TASKID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate startDate = rs.getDate("STARTDATE").toLocalDate();
        LocalDate endDate = rs.getDate("ENDDATE").toLocalDate();
        int timeEst = rs.getInt("TIMEEST");

        return new SubProject(SubProjectID, TaskID, name, description,startDate, endDate, timeEst);
    }

}
