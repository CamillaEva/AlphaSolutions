package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final SubprojectService subprojectService;

    public ProjectController(ProjectService projectService, SubprojectService subprojectService) {
        this.projectService = projectService;
        this.subprojectService = subprojectService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/create-project")
    public String createProject(Model model) {
        model.addAttribute("project", new Project());
        return "create-project";
    }

    //TODO: skal alle andre post hedde save eller skal denne ændres, så alle er ens?
    @PostMapping("/create-project")
    public String saveProject(@ModelAttribute("project") Project project) {
        projectService.createProject(project);
        return "redirect:/read-projects";
    }

    //_______________________________________________READ_______________________________________________________________

    @GetMapping("/read-projects")
    public String readAllprojects(Model model) {
        List<Project> projects = projectService.readAllProjects();
        model.addAttribute("projects", projects);
        return "read-projects";
    }

    @GetMapping("/read-project/{projectID}")
    public String readProjectByIDWithTime(@PathVariable("projectID") int projectID, Model model) {
        Project project = projectService.readProjectByID(projectID);
        int totalEstimate = 0;

        for (Subproject subProject : project.getSubProjects()) {
            int est = subprojectService.getTimeEstFromTasks(subProject.getSubProjectID());
            subProject.setTimeEst(est);
            totalEstimate += est;
        }
        model.addAttribute("project", project);
        model.addAttribute("timeEstimate", totalEstimate);
        return "read-project";
    }
    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-project/{projectID}")
    public String editProject ( @PathVariable int projectID, Model model){
        Project project = projectService.readProjectByID(projectID);
        model.addAttribute("project", project);
        return "update-project";
    }

    @PostMapping("/edit-project/{projectID}")
    public String updateProject ( @PathVariable int projectID, @ModelAttribute("project") Project project){
        projectService.updateProject(project);
        return "redirect:/read-project/" + projectID;
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-project/{projectID}")
    public String deleteProject ( @PathVariable int projectID){
        Project project = projectService.readProjectByID(projectID);
        projectService.deleteProject(project);
        return "redirect:/read-projects";
    }

}
