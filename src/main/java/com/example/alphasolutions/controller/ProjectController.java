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
public class ProjectController {
    private final ProjectService projectService;
    private final SubprojectService subprojectService;
    private final EmpService empService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, SubprojectService subprojectService,
                             EmpService empService, TaskService taskService) {
        this.projectService = projectService;
        this.subprojectService = subprojectService;
        this.empService = empService;
        this.taskService = taskService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/create-project")
    public String createProject(HttpSession session,Model model) {
        Role sessionRole = (Role) session.getAttribute("role");

        if(sessionRole == Role.PROJECT_LEADER) {
            model.addAttribute("project", new Project());
            return "create-project";
        }
        return "error/no-access";
    }

    //TODO: skal alle andre post hedde save eller skal denne ændres, så alle er ens?
    @PostMapping("/create-project")
    public String saveProject(@ModelAttribute("project") Project project, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        if (sessionRole == Role.PROJECT_LEADER) {
            projectService.createProject(project);
            return "redirect:/read-projects";
        }
        return "error/no-access";
    }

    //_______________________________________________READ_______________________________________________________________

    @GetMapping("/read-projects")
    public String readAllprojects(Model model) {
        List<Project> projects = projectService.readAllProjects();
        model.addAttribute("projects", projects);
        return "read-projects";
    }

    @GetMapping("/read-project/{projectID}")
    public String readProjectByIDWithTime(@PathVariable("projectID") int projectID,
                                          HttpSession session, Model model) {

        Role sessionRole = (Role) session.getAttribute("role");

        if(sessionRole == Role.PROJECT_LEADER || sessionRole == Role.EMPLOYEE) {
            Project project = projectService.readProjectByID(projectID);
            int totalEstimate = 0;

            for (Subproject subProject : project.getSubProjects()) {
                int est = subprojectService.getTimeEstFromTasks(subProject.getSubProjectID());
                subProject.setTimeEst(est);
                totalEstimate += est;
            }

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(projectID);
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for(int empID : assignedEmpIDsProject){
                assignedEmployeesProject.add(empService.readEmployeeById(empID));
            }

            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("project", project);
            model.addAttribute("timeEstimate", totalEstimate);
            return "read-project";
        }
        return "error/no-access";
    }
    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-project/{projectID}")
    public String editProject ( @PathVariable int projectID, HttpSession session,
                                Model model){

        Role sessionRole = (Role) session.getAttribute("role");

        if(sessionRole == Role.PROJECT_LEADER) {
            Project project = projectService.readProjectByID(projectID);
            model.addAttribute("project", project);
            return "update-project";
        }
        return "error/no-access";
    }

    @PostMapping("/edit-project/{projectID}")
    public String updateProject ( @PathVariable int projectID, @ModelAttribute("project") Project project,
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
    public String deleteProject ( @PathVariable int projectID, HttpSession session){
        Role sessionRole = (Role) session.getAttribute("role");

        if(sessionRole == Role.PROJECT_LEADER) {
            Project project = projectService.readProjectByID(projectID);
            for (Subproject s : project.getSubProjects()){
                Subproject sub = subprojectService.readSubProjectByID(s.getSubProjectID());
                for (Task t: sub.getTasks()){
                    taskService.deleteTask(t);
                }
                subprojectService.deleteSubProject(s);
            }
            projectService.deleteProject(project);
            return "redirect:/main-page/" + session.getAttribute("id");
        }
        return "error/no-access";
    }

}
