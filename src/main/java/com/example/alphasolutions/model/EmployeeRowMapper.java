package com.example.alphasolutions.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(
                rs.getInt("EMPID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("MAIL"),
                rs.getString("PASSWORD"),
                Role.valueOf(rs.getString("ROLE").toUpperCase()) // Konverter String til enum
        );
    }
}
