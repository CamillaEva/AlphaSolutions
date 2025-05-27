package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.*;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import com.example.alphasolutions.service.TaskService;
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
public class SubprojectController {
    private final ProjectService projectService;
    private final EmpService empService;
    private final SubprojectService subprojectService;
    private final TaskService taskService;

    public SubprojectController(SubprojectService subprojectService, ProjectService projectService,
                                EmpService empService, TaskService taskService) {
        this.projectService = projectService;
        this.empService = empService;
        this.taskService = taskService;
        this.subprojectService = subprojectService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/create-subproject/{projectID}")
    public String createSubproject(@PathVariable("projectID") int projectID, HttpSession session,
                                   Model model) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Project project = projectService.readProjectByID(projectID);
            model.addAttribute("project", project);
            model.addAttribute("subproject", new Subproject());
            return "create-subproject";
        }
        return "error/no-access";
    }

    @PostMapping("/create-subproject/{projectID}/add")
    public String createSubproject(@PathVariable("projectID") int projectID, @ModelAttribute Subproject subproject,
                                   HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        if (sessionRole == Role.PROJECT_LEADER) {
            subproject.setProjectID(projectID);
            int newSubprojectID = subprojectService.createSubproject(subproject);

            projectService.assignSubprojectToProject(newSubprojectID, projectID);

            return "redirect:/read-project/" + projectID;
        }
        return "error/no-access";
    }

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/read-subproject/{subprojectID}")
    public String readSubprojectByIDWithTime(@PathVariable("subprojectID") int subprojectID,
                                             HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Subproject subproject = subprojectService.readSubprojectByID(subprojectID);
            int timeEstimate = subprojectService.getTimeEstFromTasks(subprojectID);

            Project project = projectService.readProjectByID(subproject.getProjectID());
//            }
            int totalTimeEstimate = subprojectService.readTotalTimeEstimateForProject(project.getProjectID());
            //Method to get totalTimeUsed for tasks in a project
            int totalTimeUsed = subprojectService.readTotalUsedTimeForProject(project.getProjectID());

            List<Integer> assignedEmpIDsSubproject = subprojectService.showAssignedEmpSubproject(subprojectID);
            List<Employee> assignedEmployeesSubproject = new ArrayList<>();

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(subproject.getProjectID());
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for (int empID : assignedEmpIDsSubproject) {
                assignedEmployeesSubproject.add(empService.readEmployeeById(empID));
            }

            for (int empID : assignedEmpIDsProject) {
                assignedEmployeesProject.add(empService.readEmployeeById(empID));
            }

            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("assignedEmployeesSubproject", assignedEmployeesSubproject);
            model.addAttribute("subproject", subproject);
            model.addAttribute("timeEstimate", timeEstimate);
            model.addAttribute("totalTimeEstimate", totalTimeEstimate);
            model.addAttribute("totalTimeUsed", totalTimeUsed);
            return "read-subproject";
        }
        return "error/no-access";
    }


    @GetMapping("/{empID}/read-subproject/{subprojectID}/my-tasks")
    public String readMySubproject(@PathVariable int empID, @PathVariable int subprojectID, HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");
        Employee sessionEmp = (Employee) session.getAttribute("emp");

        if (sessionEmp.getEmpID() == empID && sessionRole == Role.EMPLOYEE) {
            Subproject mySubproject = subprojectService.readSubprojectByID(subprojectID);
            int timeEstimate = subprojectService.getTimeEstFromTasks(subprojectID);
            List<Task> myTasks = taskService.readMyTasks(empID, subprojectID);
            Project project = projectService.readProjectByID(mySubproject.getProjectID());

            int totalTimeEstimate = subprojectService.readTotalTimeEstimateForProject(project.getProjectID());
            //Method to get totalTimeUsed for tasks in a project
            int totalTimeUsed = subprojectService.readTotalUsedTimeForProject(project.getProjectID());

            List<Integer> assignedEmpIDsSubproject = subprojectService.showAssignedEmpSubproject(subprojectID);
            List<Employee> assignedEmployeesSubproject = new ArrayList<>();

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(mySubproject.getProjectID());
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for (int employeeID : assignedEmpIDsSubproject) {
                assignedEmployeesSubproject.add(empService.readEmployeeById(employeeID));
            }

            for (int employeeID : assignedEmpIDsProject) {
                assignedEmployeesProject.add(empService.readEmployeeById(employeeID));
            }

            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("assignedEmployeesSubproject", assignedEmployeesSubproject);
            model.addAttribute("timeEstimate", timeEstimate);
            model.addAttribute("totalTimeEstimate", totalTimeEstimate);
            model.addAttribute("totalTimeUsed", totalTimeUsed);
            model.addAttribute("mySubproject", mySubproject);
            model.addAttribute("myTasks", myTasks);
            return "read-my-subproject";
        }
        return "error/no-access";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-subproject/{subprojectID}")
    public String editSubproject(@PathVariable int subprojectID, Model model,
                                 HttpSession session) {

        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Subproject subproject = subprojectService.readSubprojectByID(subprojectID);
            model.addAttribute("subproject", subproject);
            return "update-subproject";
        }
        return "error/no-access";
    }

    @PostMapping("/edit-subproject/{subprojectID}")
    public String updateSubproject(@PathVariable int subprojectID,
                                   @ModelAttribute("subProject") Subproject subproject,
                                   HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            subprojectService.updateSubproject(subproject);
            return "redirect:/read-subproject/" + subprojectID;
        }
        return "error/no-access";
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-subproject/{subprojectID}")
    public String deleteSubproject(@PathVariable int subprojectID, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Subproject subproject = subprojectService.readSubprojectByID(subprojectID);
            subprojectService.deleteSubproject(subproject);
            return "redirect:/read-project/" + subproject.getProjectID();
        }
        return "error/no-access";
    }

}



