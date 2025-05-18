package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.Subproject;
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
public class SubprojectController {
    private final SubprojectService subProjectService;
    private final ProjectService projectService;
    private final EmpService empService;

    public SubprojectController(SubprojectService subProjectService, ProjectService projectService,
                                EmpService empService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
        this.empService = empService;
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
        if(sessionRole == Role.PROJECT_LEADER) {
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

        if (sessionRole == Role.PROJECT_LEADER || sessionRole == Role.EMPLOYEE) {
            Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
            int timeEstimate = subProjectService.getTimeEstFromTasks(subProjectID);

            Project project = projectService.readProjectByID(subProject.getProjectID());
            int totalTimeEstimate = 0;
            for (Subproject sp : project.getSubProjects()) {
                int est = subProjectService.getTimeEstFromTasks(sp.getSubProjectID());
                totalTimeEstimate += est;
            }

            List<Integer> assignedEmpIDsSubproject = subProjectService.showAssignedEmpSubproject(subProjectID);
            List<Employee> assignedEmployeesSubproject = new ArrayList<>();

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(subProject.getProjectID());
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for (int empID : assignedEmpIDsSubproject){
                assignedEmployeesSubproject.add(empService.readEmployeeById(empID));
            }

            for(int empID : assignedEmpIDsProject){
                assignedEmployeesProject.add(empService.readEmployeeById(empID));
            }

            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("assignedEmployeesSubproject", assignedEmployeesSubproject);
            model.addAttribute("subProject", subProject);
            model.addAttribute("timeEstimate", timeEstimate);
            model.addAttribute("totalTimeEstimate", totalTimeEstimate);
            return "read-subproject";
        }
        return "error/no-access";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-subproject/{subProjectID}")
    public String editSubProject(@PathVariable int subProjectID, Model model,
                                 HttpSession session) {

        Role sessionRole = (Role) session.getAttribute("role");

        if(sessionRole == Role.PROJECT_LEADER) {
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

        if(sessionRole == Role.PROJECT_LEADER) {
            subProjectService.updateSubProject(subProject);
            return "redirect:/read-subproject/" + subProjectID;
        }
        return "error/no-access";
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-subproject/{subProjectID}")
    public String deleteSubProject(@PathVariable int subProjectID, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if(sessionRole == Role.PROJECT_LEADER) {
            Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
            subProjectService.deleteSubProject(subProject.getSubProjectID());
            return "redirect:/read-subprojects";
        }
        return "error/no-access";
    }

    //___________________________________________ASSIGN EMP________________________________________________________________

}
