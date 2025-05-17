package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import com.example.alphasolutions.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    private Employee projectleader;
    private MockHttpSession session;
    private int subprojectID;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private SubprojectService subprojectService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private EmpService empService;

    @BeforeEach
    void setUp() {
        projectleader = new Employee(1, "joachim", "madsen", "joma@alphasolutions.com", "1234", Role.PROJECT_LEADER);

        subprojectID = 1;
        session = new MockHttpSession();
        session.setAttribute("emp", projectleader);
        session.setAttribute("subprojectID", subprojectID);
        session.setAttribute("role", Role.PROJECT_LEADER);
    }

    @Test
    void createTask() throws Exception{
        //Arrange
        Subproject subproject = new Subproject();
        subproject.setSubProjectID(subprojectID);

        Mockito.when(subprojectService.readSubProjectByID(subprojectID)).thenReturn(subproject);

        // Act & assert
        mockMvc.perform(get("/pl/create-task/{subProjectID}",subprojectID) //simulates HTTP get call
                .session(session)) //mock session
                .andExpect(status().isOk()) //expect no error (status is ok)
                .andExpect(view().name("create-task")) //view html
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("subProject"))
                .andExpect(model().attribute("subProject", subproject)); //expect subproject-attribute
    }

    @Test
    void saveTask() {
    }

    @Test
    void readAllTasks() {
    }

    @Test
    void readTaskByID() {
    }

    /* TODO: fix when method is used correct in case it needs to be changed
    @Test
    void readMyTasks() {
    }

     */

    @Test
    void editTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteSubProject() {
    }

    @Test
    void attachEmpToTask() {
    }

    @Test
    void testAttachEmpToTask() {
    }
}