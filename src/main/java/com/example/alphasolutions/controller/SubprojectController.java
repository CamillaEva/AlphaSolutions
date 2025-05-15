package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Project;
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
public class SubprojectController {
    private final SubprojectService subProjectService;
    private final ProjectService projectService;

    public SubprojectController(SubprojectService subProjectService, ProjectService projectService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/create-subproject/{projectID}")
    public String createSubProject(@PathVariable("projectID") int projectID, Model model) {
        Project project = projectService.readProjectByID(projectID);
        model.addAttribute("project", project);
        model.addAttribute("subproject", new Subproject());
        return "create-subproject";
    }

    @PostMapping("/create-subproject/{projectID}/add")
    public String createSubProject(@PathVariable("projectID") int projectID, @ModelAttribute Subproject subProject) {
        subProject.setProjectID(projectID);
        subProjectService.createSubProject(subProject);
        return "redirect:/read-project/" + projectID;
    }

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/read-subprojects")
    public String readAllSubProjects(Model model) {
        List<Subproject> subprojects = subProjectService.readAllSubProjects();
        model.addAttribute("subProject", subprojects);
        return "read-subprojects";
    }

    @GetMapping("/read-subproject/{subProjectID}")
    public String readSubProjectByIDWithTime(@PathVariable("subProjectID") int subProjectID, Model model) {
        Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
        int totalEstimate = subProjectService.getTimeEstFromTasks(subProjectID);

        model.addAttribute("subProject", subProject);
        model.addAttribute("timeEstimate", totalEstimate);
        return "read-subproject";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-subproject/{subProjectID}")
    public String editSubProject(@PathVariable int subProjectID, Model model) {
        Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
        model.addAttribute("subProject", subProject);
        return "update-subproject";
    }

    @PostMapping("/edit-subproject/{subProjectID}")
    public String updateSubProject(@PathVariable int subProjectID,
                                   @ModelAttribute("subProject") Subproject subProject) {
        subProjectService.updateSubProject(subProject);
        return "redirect:/read-subproject/" + subProjectID;
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-subproject/{subProjectID}")
    public String deleteSubProject ( @PathVariable int subProjectID){
        Subproject subProject = subProjectService.readSubProjectByID(subProjectID);
        subProjectService.deleteSubProject(subProject.getSubProjectID());
        return "redirect:/read-subprojects";
    }
}
