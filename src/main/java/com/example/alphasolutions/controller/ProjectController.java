package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.*;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final SubprojectService subprojectService;
    private final EmpService empService;


    public ProjectController(ProjectService projectService, SubprojectService subprojectService,
                             EmpService empService) {
        this.projectService = projectService;
        this.subprojectService = subprojectService;
        this.empService = empService;

    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/create-project")
    public String createProject(HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            model.addAttribute("project", new Project());
            return "create-project";
        }
        return "error/no-access";
    }

    @PostMapping("/create-project")
    public String saveProject(@ModelAttribute("project") Project project, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        Employee sessionEmp = (Employee) session.getAttribute("emp");

        if (sessionRole == Role.PROJECT_LEADER) {
            projectService.createProject(project);

            return "redirect:/main-page/" + sessionEmp.getEmpID();
        }
        return "error/no-access";
    }

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/{empID}/read-my-project/{projectID}")
    public String readMyProject(@PathVariable int empID, @PathVariable int projectID, HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.EMPLOYEE) {
            List<Subproject> mySubprojects = subprojectService.readMySubprojects(empID, projectID);
            Project myProject = projectService.readProjectByID(projectID);
            Employee sessionEmp = empService.readEmployeeById(empID);

            for (Subproject subproject : mySubprojects) {
                int est = subprojectService.readTimeEstFromTasks(subproject.getSubprojectID());
                subproject.setTimeEst(est);
            }

            int totalTimeEstimate = projectService.readTotalTimeEstimateForProject(projectID);
            //Method to get totalTimeUsed for tasks in a project
            int totalTimeUsed = projectService.readTotalUsedTimeForProject(projectID);

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(projectID);
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for (int employeeID : assignedEmpIDsProject) {
                assignedEmployeesProject.add(empService.readEmployeeById(employeeID));
            }

            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("totalTimeEstimate", totalTimeEstimate);
            model.addAttribute("totalTimeUsed", totalTimeUsed);
            model.addAttribute("myProject", myProject);
            model.addAttribute("sessionEmp", sessionEmp);
            model.addAttribute("mySubprojects", mySubprojects);
            return "read-my-project";
        }
        return "error/no-access";
    }

    @GetMapping("/read-project/{projectID}")
    public String readProjectByID(@PathVariable int projectID, HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");
        Employee sessionEmp = (Employee) session.getAttribute("emp");

        if (sessionRole == Role.PROJECT_LEADER) {
            List<Subproject> allSubprojects = subprojectService.readSubprojectsByProjectID(projectID);
            Project projectByID = projectService.readProjectByID(projectID);

            for (Subproject subproject : allSubprojects) {
                int est = subprojectService.readTimeEstFromTasks(subproject.getSubprojectID());
                subproject.setTimeEst(est);
            }

            int totalTimeEstimate = projectService.readTotalTimeEstimateForProject(projectID);
            int totalTimeUsed = projectService.readTotalUsedTimeForProject(projectID);

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(projectID);
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for (int employeeID : assignedEmpIDsProject) {
                assignedEmployeesProject.add(empService.readEmployeeById(employeeID));
            }

            model.addAttribute("sessionEmp", sessionEmp);
            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("project", projectByID);
            model.addAttribute("totalTimeEstimate", totalTimeEstimate);
            model.addAttribute("allSubprojects", allSubprojects);
            model.addAttribute("projectByID", projectByID);
            model.addAttribute("totalTimeUsed", totalTimeUsed);
            return "read-project";
        }
        return "error/no-access";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-project/{projectID}")
    public String editProject(@PathVariable int projectID, HttpSession session,
                              Model model) {

        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Project project = projectService.readProjectByID(projectID);
            model.addAttribute("project", project);
            return "update-project";
        }
        return "error/no-access";
    }

    @PostMapping("/edit-project/{projectID}")
    public String updateProject(@PathVariable int projectID, @ModelAttribute("project") Project project,
                                HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            projectService.updateProject(project);
            return "redirect:/read-project/" + projectID;
        }
        return "error/no-access";
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-project/{projectID}")
    public String deleteProject(@PathVariable int projectID, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Project project = projectService.readProjectByID(projectID);
            projectService.deleteProject(project);
            return "redirect:/main-page/" + session.getAttribute("id");
        }
        return "error/no-access";
    }

}
