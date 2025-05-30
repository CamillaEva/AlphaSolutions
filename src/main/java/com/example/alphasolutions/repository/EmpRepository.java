package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.EmployeeRowMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class EmpRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmpRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //-----------------------------------------LOGIN----------------------------------------------
    public void attributeSetup(HttpSession session, Employee employee) {
        session.setAttribute("emp", employee);
        session.setAttribute("role", employee.getRole());
        session.setAttribute("id", employee.getEmpID());
        session.setMaxInactiveInterval(300);
    }

    //-------------------------------------CREATE----------------------------------------------
    public int createEmployee(Employee employee) {
        String sql = "INSERT INTO EMP (FIRSTNAME, LASTNAME, MAIL, PASSWORD, ROLE) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getMail());
            ps.setString(4, employee.getPassword());
            ps.setString(5, employee.getRole().toString().toUpperCase());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            return key.intValue();
        } else {
            throw new IllegalArgumentException();
        }
    }

    //_______________________________________________READ_______________________________________________________________
    public List<Employee> readAllEmployees() {
        String sql = "SELECT EMPID, FIRSTNAME, LASTNAME, MAIL, PASSWORD, ROLE FROM emp";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    public Employee readEmployeeById(int empId) {
        String sql = "SELECT EMPID, FIRSTNAME, LASTNAME, MAIL, PASSWORD, ROLE FROM emp WHERE EMPID = ?";
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), empId);
    }

    public Employee signIn(String mail, String password) {
        try {
            String sql = "SELECT * FROM EMP WHERE MAIL = ? AND PASSWORD = ?";
            return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), mail, password);
        } catch (EmptyResultDataAccessException o) {
            return null;
        }
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateEmployee(Employee employee) {
        String sql = "UPDATE EMP SET FIRSTNAME = ?, LASTNAME = ?, MAIL = ?, PASSWORD = ?, ROLE = ? WHERE EMPID = ?";
        jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getMail(),
                employee.getPassword(), employee.getRole().name(), employee.getEmpID());
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteEmployee(Employee employee) {
        String sql = "DELETE FROM EMP WHERE EMPID = ?";
        jdbcTemplate.update(sql, employee.getEmpID());
    }

}
