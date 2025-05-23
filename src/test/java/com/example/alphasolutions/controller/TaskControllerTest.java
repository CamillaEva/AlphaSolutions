package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.Subproject;
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
    void createTask() throws Exception{
        //Arrange
        Subproject subproject = new Subproject();
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
    void updateUsedTime() throws Exception{
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
    void deleteTask() throws Exception{
        int taskID = 1;
        Task task = new Task();
        task.setTaskID(taskID);
        task.setSubProjectID(2);


        Mockito.when(taskService.readTaskByID(taskID)).thenReturn(task);
        doNothing().when(taskService).deleteTask(task);

        mockMvc.perform(post("/delete-task/{taskID}", taskID).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/read-subproject/" + task.getSubProjectID()));

        Mockito.verify(taskService).deleteTask(task);
    }

    @Test
    void assignEmpToTask() throws Exception{
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        List<Integer> alreadyAssigned = Arrays.asList(1,2);

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

    /*
       @PostMapping("/read-tasks/{taskID}/assign-emp")
    public String assignEmpToTask(@PathVariable int taskID,
                                  @RequestParam("empSelected") List<Integer> selectedEmpIDs,
                                  HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            if (selectedEmpIDs != null) {
                for (int empID : selectedEmpIDs) {
                    taskService.assignEmpToTask(taskID, empID);
                }
            }
            return "redirect:/read-tasks/" + taskID;
        }
        return "error/no-access";
    }

     */
    @Test
    void testAssignEmpToTask() {


    }
}