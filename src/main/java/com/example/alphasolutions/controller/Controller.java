package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.SubProject;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubProjectService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private final EmpService empService;
    private final ProjectService projectService;
    private final SubProjectService subProjectService;

    public Controller(EmpService empService, ProjectService projectService, SubProjectService subProjectService) {
        this.empService = empService;
        this.projectService = projectService;
        this.subProjectService = subProjectService;
    }

    @GetMapping("/home")
    public String homePage() {
        return "home"; // Dette refererer til home.html
    }


    //____________________________________CREATE ____________________________________
    @GetMapping("/home/create-employee")
    public String createEmployee(Model model) {
        model.addAttribute("emp", new Employee());
        model.addAttribute("roles", Role.values());
        return "create-employee";
    }

    @PostMapping("/home/create-employee")
    public String saveEmployee(@ModelAttribute("emp") Employee employee) {
        empService.createEmployee(employee);
        return "redirect:/home/read-employees";
    }


  @GetMapping("/create-project")
  public String createProject(Model model){
        model.addAttribute("project", new Project());
        return "create-project";
  }

  @PostMapping("/create-project")
  public String saveProject(@ModelAttribute("project") Project project){
        projectService.createProject(project);
        return "redirect:/read-projects";
  }

    @GetMapping("/create-subproject")
    public String createSubProject(Model model){
        model.addAttribute("subproject", new SubProject());
        return "create-subproject";
    }

    @PostMapping("/create-subproject")
    public String saveSubProject(@ModelAttribute("subProject") SubProject subProject){
        subProjectService.createSubProject(subProject);
        return "redirect:/read-subprojects";
    }




    //____________________________________READ ______________________________________
    @GetMapping("/home/read-employees")
    public String readAllEmployees(Model model) {
        List<Employee> employees = empService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "read-employees";
    }

    @GetMapping("/home/read-employee/{empId}")
    public String readEmployeeById(@PathVariable int empId, Model model) {
        Employee employee = empService.readEmployeeById(empId);
        model.addAttribute("employee", employee);
        return "read-employee";
    }

    @GetMapping("/read-projects")
    public String readAllprojects(Model model){
        List<Project> projects = projectService.readAllProjects();
        model.addAttribute("projects", projects);
        return "read-projects";
    }


    @GetMapping("/read-project/{projectID}")
    public String getProjectByID(@PathVariable int projectID, Model model){
        Project project = projectService.getProjectByID(projectID);
        model.addAttribute("project", project);
        return "read-project";
    }

    @GetMapping("/read-subprojects")
    public String readAllSubProjects(Model model){
        List<SubProject> subProjects = subProjectService.readAllSubProjects();
        model.addAttribute("subProject", subProjects);
        return "read-subprojects";
    }

    @GetMapping("/read-subproject/{subProjectID}")
    public String getSubProjectByID(@PathVariable int subProjectID, Model model){
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);
        model.addAttribute("subProject", subProject);
        return "read-subproject";
    }





    //____________________________________UPDATE ____________________________________
    //Mapping to edit employees data
    @GetMapping("/home/edit-employee/{empId}")
    public String editEmployee(@PathVariable int empId, Model model) {
        Employee employee = empService.readEmployeeById(empId);
        model.addAttribute("employee", employee);
        model.addAttribute("roles", Role.values());
        return "update-employee";
    }

    //Mapping to UPDATE employees data
    @PostMapping("/home/edit-employee/{empId}")
    public String updateEmployee(@PathVariable int empId, @ModelAttribute("employee") Employee employee) {
        // Konverter enum til en String
        String roleAsString = employee.getRole().name(); // 'ADMIN', 'USER', etc.
        // Sæt rollen som en String i Employee objektet
        employee.setRole(Role.valueOf(roleAsString)); // Konverter tilbage til enum, hvis nødvendigt

        empService.updateEmployee(employee);
        return "redirect:/home/read-employee/" + empId;
    }


    @GetMapping("/edit-project/{projectID}")
    public String editProject(@PathVariable int projectID, Model model){
        Project project = projectService.getProjectByID(projectID);
        model.addAttribute("project", project);
        return "update-project";
    }

    @PostMapping("/edit-project/{projectID}")
    public String updateProject (@PathVariable int projectID, @ModelAttribute("project") Project project){
        projectService.updateProject(project);
        return "redirect:/read-project/" + projectID;
    }

    @GetMapping("/edit-subproject/{subProjectID}")
    public String editSubProject(@PathVariable int subProjectID, Model model){
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);
        model.addAttribute("subProject", subProject);
        return "update-subproject";
    }

    @PostMapping("/edit-subproject/{subProjectID}")
    public String updateSubProject (@PathVariable int subProjectID, @ModelAttribute("subProject") SubProject subProject){
        subProjectService.updateSubProject(subProject);
        return "redirect:/read-subproject/" + subProjectID;
    }



    //____________________________________DELETE____________________________________

    @PostMapping("/delete-project/{projectID}")
    public String deleteProject(@PathVariable int projectID){
        Project project = projectService.getProjectByID(projectID);
        projectService.deleteProject(project);
        return "redirect:/read-projects";
    }

    @PostMapping("/delete-subproject/{subProjectID}")
    public String deleteSubProject(@PathVariable int subProjectID){
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);
        subProjectService.deleteSubProject(subProject.getSubProjectID());
        return "redirect:/read-subprojects";
    }
}
