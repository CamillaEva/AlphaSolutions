package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Admin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminRepository {

    public AdminRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                System.getenv("DB_URL"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_PASSWORD")
        );
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    }

    List<Admin> admins = new ArrayList<>(List.of(new Admin("Admin", "1234")));

    public Admin getAdmin(String mail) {
        for (Admin admin : admins) {
            if (admin.getMail().equals(mail))
                return admin;
        }
        return null;
    }

}
