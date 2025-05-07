package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.EmployeeRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class EmpRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmpRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_PASSWORD")
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
            ps.setString(5, employee.getRole().toString().toUpperCase()); //Konvertere ENUM til en string
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    //--------------------------------------READ------------------------------------------------
    public List<Employee> getAllEmployees() {
        String sql = "SELECT EMPID, FIRSTNAME, LASTNAME, MAIL, PASSWORD, ROLE FROM emp";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    public Employee readEmployeeById(int empId) {
        String sql = "SELECT EMPID, FIRSTNAME, LASTNAME, MAIL, PASSWORD, ROLE FROM emp WHERE EMPID = ?";
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), empId);
    }

    //------------------------------------UPDATE-----------------------------------------------------------------
    public void updateEmployee(Employee employee) {
        String sql = "UPDATE EMP SET FIRSTNAME = ?, LASTNAME = ?, MAIL = ?, PASSWORD = ?, ROLE = ? WHERE EMPID = ?";
        jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getMail(),
                employee.getPassword(), employee.getRole().name(), employee.getEmpID());
    }

    //------------------------------------DELETE-----------------------------------------------------------------
    public void deleteEmployee(Employee employee) {
        String sql = "DELETE FROM EMP WHERE EMPID = ?";
        jdbcTemplate.update(sql, employee.getEmpID());
    }

}
