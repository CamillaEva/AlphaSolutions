package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.*;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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
    void createTask() throws Exception {
        //Arrange
        Subproject subproject = new Subproject();
        subproject.setSubprojectID(subprojectID);

        Mockito.when(subprojectService.readSubprojectByID(subprojectID)).thenReturn(subproject);

        // Act & assert
        mockMvc.perform(get("/create-task/{subProjectID}", subprojectID)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("create-task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("subProject"))
                .andExpect(model().attribute("subProject", subproject));
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
    void readTaskByID() throws Exception {
        int taskID = 1;
        int subprojectID = 1;
        int projectID = 1;
        Task task = new Task(taskID, "test", "test task object", LocalDate.of(2025, 5, 14), LocalDate.of(2025, 5, 28), 10, subprojectID, 2);

        Subproject subproject = new Subproject(subprojectID, projectID, "test", "test subproject object", LocalDate.of(2025, 5, 14), LocalDate.of(2025, 5, 28));

        Project project = new Project(projectID, "test", "test project object", LocalDate.of(2025, 5, 14), LocalDate.of(2025, 5, 28));

        List<Integer> assignedEmpIDsTask = List.of(1);

        List<Integer> assignedEmpIDsProject = List.of(1);

        Mockito.when(taskService.readTaskByID(taskID)).thenReturn(task);
        Mockito.when(subprojectService.readSubprojectByID(subprojectID)).thenReturn(subproject);
        Mockito.when(projectService.readProjectByID(projectID)).thenReturn(project);
        Mockito.when(taskService.readTotalTimeEstimateForProject(projectID)).thenReturn(10);
        Mockito.when(taskService.readTotalUsedTimeForProject(projectID)).thenReturn(2);
        Mockito.when(taskService.showAssignedEmpTask(taskID)).thenReturn(assignedEmpIDsTask);
        Mockito.when(empService.readEmployeeById(1)).thenReturn(projectleader);
        Mockito.when(projectService.showAssignedEmpProject(projectID)).thenReturn(assignedEmpIDsProject);

        mockMvc.perform(get("/read-tasks/{taskID}", taskID).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("read-task"))
                .andExpect(model().attributeExists("subproject"))
                .andExpect(model().attributeExists("sessionEmp"))
                .andExpect(model().attributeExists("assignedEmployeesProject"))
                .andExpect(model().attributeExists("assignedEmployeesTask"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("totalTimeEstimate"))
                .andExpect(model().attributeExists("totalTimeUsed"));
    }

    @Test
    void editTask() throws Exception {
        Task task = new Task();
        int taskID = 1;
        task.setTaskID(taskID);

        Mockito.when(taskService.readTaskByID(taskID)).thenReturn(task);

        mockMvc.perform(get("/pl/edit-task/{taskID}", taskID).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("update-task"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("task", task));
    }

    @Test
    void updateTask() throws Exception {
        int taskID = 1;
        Task task = new Task();
        task.setTaskID(taskID);

        doNothing().when(taskService).updateTask(task); //.doNothing() used for void method

        mockMvc.perform(post("/pl/edit-task/{taskID}", taskID).session(
                        session).flashAttr("task", task)) //.flashAttr simulates the task object that the user would have submitted in the html form
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/read-tasks/" + taskID));

        Mockito.verify(taskService).updateTask(task);
    }

    @Test
    void updateUsedTime() throws Exception {
        int taskID = 1;
        Task task = new Task();
        task.setTaskID(taskID);

        doNothing().when(taskService).updateUsedTime(task);

        mockMvc.perform(post("/emp/edit-task/{taskID}", taskID).session(session).flashAttr("task", task))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/read-tasks/" + taskID));

        Mockito.verify(taskService).updateUsedTime(task);
    }

    @Test
    void deleteTask() throws Exception {
        int taskID = 1;
        Task task = new Task();
        task.setTaskID(taskID);
        task.setSubprojectID(2);


        Mockito.when(taskService.readTaskByID(taskID)).thenReturn(task);
        doNothing().when(taskService).deleteTask(task);

        mockMvc.perform(post("/delete-task/{taskID}", taskID).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/read-subproject/" + task.getSubprojectID()));

        Mockito.verify(taskService).deleteTask(task);
    }

    @Test
    void assignEmpToTask() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        List<Integer> alreadyAssigned = Arrays.asList(1, 2);

        int taskID = 1;
        Task task = new Task();
        task.setTaskID(taskID);

        Mockito.when(empService.readAllEmployees()).thenReturn(employees);
        Mockito.when(taskService.showAssignedEmpTask(taskID)).thenReturn(alreadyAssigned);

        mockMvc.perform(get("/read-tasks/{taskID}/assign-emp", taskID).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("assign-emp"))
                .andExpect(model().attributeExists("alreadyAssigned"))
                .andExpect(model().attributeExists("taskID"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attribute("employees", employees))
                .andExpect(model().attribute("taskID", taskID))
                .andExpect(model().attribute("alreadyAssigned", alreadyAssigned));
    }

    @Test
    void testAssignEmpToTask() throws Exception {
        int taskID = 1;

        mockMvc.perform(post("/read-tasks/{taskID}/assign-emp", taskID).session(session).param("empSelected",
                        "4", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/read-tasks/" + taskID));

        Mockito.verify(taskService).assignEmpToTask(taskID, 4);
        Mockito.verify(taskService).assignEmpToTask(taskID, 5);
    }
}