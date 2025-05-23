package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.SubProject;
import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import com.example.alphasolutions.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

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
        SubProject subproject = new SubProject();
        subproject.setSubProjectID(subprojectID);

        Mockito.when(subprojectService.readSubProjectByID(subprojectID)).thenReturn(subproject);

        // Act & assert
        mockMvc.perform(get("/create-task/{subProjectID}",subprojectID) //simulates HTTP get call
                        .session(session)) //mock session
                .andExpect(status().isOk()) //expect no error (status is ok)
                .andExpect(view().name("create-task")) //view html
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("subProject"))
                .andExpect(model().attribute("subProject", subproject)); //expect subproject-attribute
    }

    @Test
    void saveTask() throws Exception {
        int newTaskID = 42;

        //the new task just need to be an instance of the Task class
        Mockito.when(taskService.createTask(Mockito.any(Task.class))).thenReturn(newTaskID);

        mockMvc.perform(post("/create-task/{subProjectID}/add", subprojectID)
                        .session(session)
                        .param("name", "wishlist")
                        .param("description", "digital wishlist")
                        .param("startDate", "2025-10-02")
                        .param("endDate", "2025-11-05")
                        .param("timeEst", "10")
                        .param("subprojectID", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/read-subproject/" + subprojectID));

        Mockito.verify(taskService).createTask(Mockito.any(Task.class));
        Mockito.verify(subprojectService).assignTaskToSubproject(newTaskID, subprojectID);
    }

    @Test
    void readTaskByID() {



    }

    @Test
    void editTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateUsedTime() {
    }

    @Test
    void deleteSubProject() {
    }

    @Test
    void assignEmpToTask() {
    }

    @Test
    void testAssignEmpToTask() {
    }
}