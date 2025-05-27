package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.*;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    private Employee projectleader;
    private MockHttpSession session;
    private int projectID;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpService empService;

    @MockBean
    private SubprojectService subprojectService;

    @MockBean
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectleader = new Employee(1, "Mads", "Jørgensen", "majo@alphasolutions.com", "1234", Role.PROJECT_LEADER);

        projectID = 1;
        session = new MockHttpSession();
        session.setAttribute("emp", projectleader);
        session.setAttribute("projectID", projectID);
        session.setAttribute("role", Role.PROJECT_LEADER);
    }

    @Test
    void createProjectSuccess() throws Exception {
        //Arrange

        //Act
        ResultActions ra = mockMvc.perform(get("/create-project").session(session));

        //Assert

        ra.andExpect(status().isOk());
        ra.andExpect(view().name("create-project"));
        ra.andExpect(model().attributeExists("project"));

    }


    @Test
    void createProjectRoleFail() throws Exception {
        //Arrange
        session.setAttribute("role", Role.EMPLOYEE);

        //Act
        ResultActions ra = mockMvc.perform(get("/create-project").session(session));

        //Assert

        ra.andExpect(status().isOk());
        ra.andExpect(view().name("error/no-access"));

    }

    @Test
    void saveProjectSuccess() throws Exception {
        //Arrange
        Project project = new Project();
        Mockito.when(projectService.createProject(Mockito.any(Project.class))).thenReturn(projectID);

        //Act
        ResultActions ra = mockMvc.perform(post("/create-project").session(session).requestAttr("project", project));

        //Assert
        ra.andExpect(status().is3xxRedirection());
        ra.andExpect(view().name("redirect:/main-page/" + projectleader.getEmpID()));

    }

    @Test
    void saveProjectRoleFail() throws Exception {
        //Arrange
        Project project = new Project();
        session.setAttribute("role", Role.EMPLOYEE);
        Mockito.when(projectService.createProject(Mockito.any(Project.class))).thenReturn(projectID);

        //Act
        ResultActions ra = mockMvc.perform(post("/create-project").session(session).requestAttr("project", project));

        //Assert
        ra.andExpect(status().isOk());
        ra.andExpect(view().name("error/no-access"));

    }


    @Test
    void readMyProject() throws Exception {
        //Arrange
        int empID = 1;
        int projectID = 2;
        session.setAttribute("role", Role.EMPLOYEE);
        Project project = new Project();
        Employee emp = new Employee(2, "Caroline", "Eva", "caev@alphasolutions.com", "1234", Role.EMPLOYEE);
        List<Subproject> subprojects = List.of(new Subproject());
        List<Integer> empIDs = List.of(3, 4);
        List<Employee> emps = List.of(new Employee(), new Employee());

        Mockito.when(projectService.readProjectByID(projectID)).thenReturn(project);
        Mockito.when(empService.readEmployeeById(empID)).thenReturn(emp);
        Mockito.when(subprojectService.readMySubprojects(empID, projectID)).thenReturn(subprojects);
        Mockito.when(subprojectService.readTimeEstFromTasks(Mockito.anyInt())).thenReturn(10);
        Mockito.when(projectService.readTotalTimeEstimateForProject(projectID)).thenReturn(40);
        Mockito.when(projectService.readTotalUsedTimeForProject(projectID)).thenReturn(20);
        Mockito.when(projectService.showAssignedEmpProject(projectID)).thenReturn(empIDs);
        Mockito.when(empService.readEmployeeById(3)).thenReturn(emps.get(0));
        Mockito.when(empService.readEmployeeById(4)).thenReturn(emps.get(1));


        //Act
        ResultActions ra = mockMvc.perform(get("/1/read-my-project/2").session(session));

        //Assert
        ra.andExpect(status().isOk());
        ra.andExpect(view().name("read-my-project"));
        ra.andExpect(model().attributeExists(
                "assignedEmployeesProject",
                "totalTimeEstimate",
                "totalTimeUsed",
                "myProject",
                "sessionEmp",
                "mySubprojects"
        ));

    }


    @Test
    void readProjectByID() throws Exception {
        //Arrange
        int projectID = 2;

        Project project = new Project();
        List<Subproject> subprojects = List.of(new Subproject());
        List<Integer> empIDs = List.of(5, 6);
        List<Employee> emps = List.of(new Employee(), new Employee());

        Mockito.when(projectService.readProjectByID(projectID)).thenReturn(project);
        Mockito.when(subprojectService.readSubprojectsByProjectID(projectID)).thenReturn(subprojects);
        Mockito.when(subprojectService.readTimeEstFromTasks(Mockito.anyInt())).thenReturn(8);
        Mockito.when(projectService.readTotalTimeEstimateForProject(projectID)).thenReturn(30);
        Mockito.when(projectService.readTotalUsedTimeForProject(projectID)).thenReturn(15);
        Mockito.when(projectService.showAssignedEmpProject(projectID)).thenReturn(empIDs);
        Mockito.when(empService.readEmployeeById(5)).thenReturn(emps.get(0));
        Mockito.when(empService.readEmployeeById(6)).thenReturn(emps.get(1));

        //Act
        ResultActions ra = mockMvc.perform(get("/read-project/" + projectID).session(session));

        //Assert

        ra.andExpect(status().isOk());
        ra.andExpect(view().name("read-project"));
        ra.andExpect(model().attributeExists(
                "sessionEmp",
                "assignedEmployeesProject",
                "project",
                "totalTimeEstimate",
                "allSubprojects",
                "projectByID",
                "totalTimeUsed"
        ));
    }


    @Test
    void editProjectSuccess() throws Exception {
        //Arrange
        Project project = new Project();
        Mockito.when(projectService.readProjectByID(projectID)).thenReturn(project);

        //Act
        ResultActions ra = mockMvc.perform(get("/edit-project/" + projectID).session(session));

        //Assert
        ra.andExpect(status().isOk());
        ra.andExpect(view().name("update-project"));
    }


    @Test
    void editProjectRoleFail() throws Exception {
        //Arrange
        session.setAttribute("role", Role.EMPLOYEE);
        Project project = new Project();
        Mockito.when(projectService.readProjectByID(projectID)).thenReturn(project);

        //Act
        ResultActions ra = mockMvc.perform(get("/edit-project/" + projectID).session(session));

        //Assert
        ra.andExpect(status().isOk());
        ra.andExpect(view().name("error/no-access"));
    }

    @Test
    void updateProjectSuccess() throws Exception {
        //Arrange
        Project project = new Project();
        doNothing().when(projectService).updateProject(project);

        //Act
        ResultActions ra = mockMvc.perform(post("/edit-project/" + projectID).session(session));

        //Assert
        ra.andExpect(status().is3xxRedirection());
        ra.andExpect(view().name("redirect:/read-project/" + projectID));
    }

    @Test
    void updateProjectRoleFail() throws Exception {
        //Arrange
        session.setAttribute("role", Role.EMPLOYEE);
        Project project = new Project();
        doNothing().when(projectService).updateProject(project);

        //Act
        ResultActions ra = mockMvc.perform(post("/edit-project/" + projectID).session(session));

        //Assert
        ra.andExpect(status().isOk());
        ra.andExpect(view().name("error/no-access"));
    }

    @Test
    void deleteProjectSuccess() throws Exception {
        //Arrange

        Project project = new Project();
        Mockito.when(projectService.readProjectByID(projectID)).thenReturn(project);
        doNothing().when(projectService).deleteProject(project);

        //Act
        ResultActions ra = mockMvc.perform(post("/delete-project/{projectID}", projectID).session(session));

        //Assert
        ra.andExpect(status().is3xxRedirection());
        ra.andExpect(redirectedUrl("/main-page/" + session.getAttribute("empID")));

    }


    @Test
    void deleteProjectRoleFail() throws Exception {
        //Arrange
        session.setAttribute("role", Role.EMPLOYEE);
        Project project = new Project();
        Mockito.when(projectService.readProjectByID(projectID)).thenReturn(project);
        doNothing().when(projectService).deleteProject(project);

        //Act
        ResultActions ra = mockMvc.perform(post("/delete-project/{projectID}", projectID).session(session));

        //Assert
        ra.andExpect(status().isOk());
        ra.andExpect(view().name("error/no-access"));

    }
}