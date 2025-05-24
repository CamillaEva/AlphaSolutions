package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
class EmpRepositoryTest {

    @Autowired
    EmpRepository empRepository;

    @Test
    void attributeSetup() {
        Employee employee = empRepository.signIn("bola@alphasolutions.com", "test123");
        MockHttpSession session = new MockHttpSession();

        empRepository.attributeSetup(session, employee);
        assertEquals(employee, session.getAttribute("emp"));
        assertEquals(employee.getRole(), session.getAttribute("role"));
        assertEquals(employee.getEmpID(), session.getAttribute("id"));
        assertEquals(300, session.getMaxInactiveInterval());
    }

    @Test
    void createEmployee() {
        Employee employee = new Employee(4, "test", "db", "test@alphasolutions.com", "1234", Role.PROJECT_LEADER);
        empRepository.createEmployee(employee);

        int expected = 4;
        int actual = empRepository.readAllEmployees().size();

        assertEquals(expected, actual);
    }

    @Test
    void readAllEmployees() {

        int expected = 3;
        int actual = empRepository.readAllEmployees().size();

        assertEquals(expected, actual);
    }

    @Test
    void readEmployeeById() {
        Employee employee = empRepository.readEmployeeById(1);

        assertNotNull(employee);
        assertEquals("Anna", employee.getFirstName());
        assertEquals("Andersen", employee.getLastName());
        assertEquals("anan@alphasolutions.com", employee.getMail());
        assertEquals("test123", employee.getPassword());
        assertEquals(Role.PROJECT_LEADER, employee.getRole());
    }

    @Test
    void signInSuccess() {
        String mail = "bola@alphasolutions.com";
        String password = "test123";

        Employee employee = empRepository.signIn(mail, password);

        assertNotNull(employee);
        assertEquals("Bob", employee.getFirstName());
        assertEquals("Larsen", employee.getLastName());
        assertEquals(Role.EMPLOYEE, employee.getRole());
    }

    @Test
    void signInWrongPassword() {
        String mail = "bola@alphasolutions.com";
        String password = "wrongPassword";

        Employee employee = empRepository.signIn(mail, password);

        assertNull(employee);
    }

    @Test
    void updateEmployee() {
        String expectedFirstName = "Klara";
        String expectedLastName = "Solstråle";
        String expectedMail = "KlaraSolstråle@alphasolutions.com";
        String expectedPassword = "Bandit123";
        Role expectedRole = Role.PROJECT_LEADER;

        Employee emp = empRepository.readEmployeeById(1);
        emp.setFirstName(expectedFirstName);
        emp.setLastName(expectedLastName);
        emp.setMail(expectedMail);
        emp.setPassword(expectedPassword);
        emp.setRole(expectedRole);

        empRepository.updateEmployee(emp);
        Employee updatedEmp = empRepository.readEmployeeById(1);

        assertEquals(expectedFirstName, updatedEmp.getFirstName());
        assertEquals(expectedLastName, updatedEmp.getLastName());
        assertEquals(expectedMail, updatedEmp.getMail());
        assertEquals(expectedPassword, updatedEmp.getPassword());
        assertEquals(expectedRole, updatedEmp.getRole());
    }

    @Test
    void deleteEmployee() {
        empRepository.deleteEmployee(empRepository.readEmployeeById(1));

        int expected = 2;
        int actual = empRepository.readAllEmployees().size();

        assertEquals(expected, actual);
    }
}