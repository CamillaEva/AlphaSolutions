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
    private final SubprojectService subProjectService;
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
    public String createSubProject(@PathVariable("projectID") int projectID, HttpSession session,
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
    public String createSubProject(@PathVariable("projectID") int projectID, @ModelAttribute Subproject subProject,
                                   HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        if (sessionRole == Role.PROJECT_LEADER) {
            subProject.setProjectID(projectID);
            int newSubprojectID = subProjectService.createSubProject(subProject);

            projectService.assignSubprojectToProject(newSubprojectID, projectID);

            return "redirect:/read-project/" + projectID;
        }
        return "error/no-access";
    }

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/read-subproject/{subProjectID}")
    public String readSubProjectByIDWithTime(@PathVariable("subProjectID") int subProjectID,
                                             HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
            int timeEstimate = subProjectService.getTimeEstFromTasks(subProjectID);

            Project project = projectService.readProjectByID(subProject.getProjectID());
//            }
            int totalTimeEstimate = subProjectService.readTotalTimeEstimateForProject(project.getProjectID());
            //Method to get totalTimeUsed for tasks in a project
            int totalTimeUsed = subProjectService.readTotalUsedTimeForProject(project.getProjectID());

            List<Integer> assignedEmpIDsSubproject = subProjectService.showAssignedEmpSubproject(subProjectID);
            List<Employee> assignedEmployeesSubproject = new ArrayList<>();

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(subProject.getProjectID());
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for (int empID : assignedEmpIDsSubproject) {
                assignedEmployeesSubproject.add(empService.readEmployeeById(empID));
            }

            for (int empID : assignedEmpIDsProject) {
                assignedEmployeesProject.add(empService.readEmployeeById(empID));
            }

            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("assignedEmployeesSubproject", assignedEmployeesSubproject);
            model.addAttribute("subProject", subProject);
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
            Subproject mySubproject = subprojectService.readSubProjectByID(subprojectID);
            int timeEstimate = subProjectService.getTimeEstFromTasks(subprojectID);
            List<Task> myTasks = taskService.readMyTasks(empID, subprojectID);

            //TODO SKAL SLETTES
            List<Subproject> subprojects = subprojectService.readMySubprojects(empID, mySubproject.getProjectID());

            int totalTimeEstimate = 0;
            for (Subproject sp : subprojects) {
                int est = subProjectService.getTimeEstFromTasks(sp.getSubProjectID());
                totalTimeEstimate += est;
            }

            List<Integer> assignedEmpIDsSubproject = subProjectService.showAssignedEmpSubproject(subprojectID);
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

            model.addAttribute("mySubproject", mySubproject);
            model.addAttribute("myTasks", myTasks);
            return "read-my-subproject";
        }
        return "error/no-access";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-subproject/{subProjectID}")
    public String editSubProject(@PathVariable int subProjectID, Model model,
                                 HttpSession session) {

        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
            model.addAttribute("subProject", subProject);
            return "update-subproject";
        }
        return "error/no-access";
    }

    @PostMapping("/edit-subproject/{subProjectID}")
    public String updateSubProject(@PathVariable int subProjectID,
                                   @ModelAttribute("subProject") Subproject subProject,
                                   HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            subProjectService.updateSubProject(subProject);
            return "redirect:/read-subproject/" + subProjectID;
        }
        return "error/no-access";
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-subproject/{subProjectID}")
    public String deleteSubProject(@PathVariable int subProjectID, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
            subProjectService.deleteSubProject(subProject);
            return "redirect:/read-project/" + subProject.getProjectID();
        }
        return "error/no-access";
    }

}



