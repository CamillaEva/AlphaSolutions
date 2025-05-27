package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.service.EmpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpController.class)
class EmpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpService empService;

    private MockHttpSession session;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1, "John", "Wick", "jw@alphasolutions.com",
                "1234", Role.PROJECT_LEADER);

        session = new MockHttpSession();
        session.setAttribute("role", Role.ADMIN);
        session.setAttribute("id", employee.getEmpID());
    }

    @Test
    void createEmployee() throws Exception {

        mockMvc.perform(get("/create-employee").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("create-employee"))
                .andExpect(model().attributeExists("emp"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("sessionID"));
    }

    @Test
    void saveEmployee() throws Exception {
        when(empService.createEmployee(any(Employee.class))).thenReturn(employee.getEmpID());

        mockMvc.perform(post("/create-employee")
                        .session(session)
                        .param("empID", String.valueOf(employee.getEmpID()))
                        .param("firstName", employee.getFirstName())
                        .param("lastName", employee.getLastName())
                        .param("mailInitials", "jw")
                        .param("password", employee.getPassword())
                        .param("role", Role.PROJECT_LEADER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main-page/" + session.getAttribute("id")));
    }

    @Test
    void readAllEmployees() throws Exception {
        List<Employee> employeeList = List.of(employee, new Employee(1, "test", "test", "test@alphasolutions.com",
                "1234", Role.EMPLOYEE));

        when(empService.readAllEmployees()).thenReturn(employeeList);

        session.setAttribute("emp", employee);

        mockMvc.perform(get("/read-employees").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("read-employees"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees", employeeList));
    }

    @Test
    void readEmployeeById() throws Exception {
        int empID = 1;

        when(empService.readEmployeeById(empID)).thenReturn(employee);

        session.setAttribute("emp", employee);
        session.setAttribute("role", Role.ADMIN);
        session.setAttribute("id", empID);

        mockMvc.perform(get("/read-employee/{empId}", empID).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("read-employee"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("sessionID"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("sessionID", session.getAttribute("id")));
    }

    @Test
    void editEmployee() throws Exception {
        int empID = employee.getEmpID();

        when(empService.readEmployeeById(empID)).thenReturn(employee);

        session.setAttribute("role", Role.ADMIN);
        session.setAttribute("id", empID);

        mockMvc.perform(get("/edit-employee/{empId}", empID)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("update-employee"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("employee", employee));
    }

    @Test
    void updateEmployee() throws Exception {
        int empID = 1;

        session.setAttribute("role", Role.ADMIN);
        session.setAttribute("id", empID);

        doNothing().when(empService).updateEmployee(any(Employee.class));

        mockMvc.perform(post("/edit-employee/{empId}", empID)
                        .session(session)
                        .param("empID", String.valueOf(empID))
                        .param("firstName", "updatedFirstName")
                        .param("lastName", "updatedLastName")
                        .param("mail", "updated@alphasolutions.com")
                        .param("password", "updatedPassword1234")
                        .param("role", "ADMIN")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/read-employee/" + empID));
        verify(empService).updateEmployee(any(Employee.class));

    }

    @Test
    void deleteEmployee() throws Exception {
        int empID = employee.getEmpID();

        session.setAttribute("role", Role.ADMIN);
        session.setAttribute("id", empID);

        when(empService.readEmployeeById(empID)).thenReturn(employee);
        doNothing().when(empService).deleteEmployee(any(Employee.class));

        mockMvc.perform(post("/delete-employee/{empId}", empID)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main-page/" + session.getAttribute("id")));

        verify(empService).readEmployeeById(empID);
        verify(empService).deleteEmployee(employee);
    }
}