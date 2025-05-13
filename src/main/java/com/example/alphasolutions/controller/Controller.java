package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.SubProject;
import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubProjectService;
import com.example.alphasolutions.service.TaskService;
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
    private final TaskService taskService;


    public Controller(EmpService empService, TaskService taskService, ProjectService projectService, SubProjectService subProjectService) {
        this.empService = empService;
        this.taskService = taskService;
        this.projectService = projectService;
        this.subProjectService = subProjectService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/admin/create-employee")
    public String createEmployee(Model model) {
        model.addAttribute("emp", new Employee());
        model.addAttribute("roles", Role.values());
        return "create-employee";
    }

    @PostMapping("/admin/create-employee")
    public String saveEmployee(@ModelAttribute("emp") Employee employee) {
        empService.createEmployee(employee);
        return "redirect:/admin";
    }

    @GetMapping("/pl/create-task/{subProjectID}")
    public String createTask(@PathVariable("subProjectID") int subProjectID, Model model) {
        SubProject subProject = subProjectService.readSubProjectByID(subProjectID);
        model.addAttribute("subProject", subProject);
        model.addAttribute("task", new Task());
        return "create-task";
    }

    @PostMapping("/pl/create-task/{subProjectID}/add")
    public String saveTask(@PathVariable("subProjectID") int subProjectID, @ModelAttribute Task task) {
        task.setSubProjectID(subProjectID);
        taskService.createTask(task);
        return "redirect:/read-subproject/" + subProjectID;
    }

    @GetMapping("/create-project")
    public String createProject(Model model) {
        model.addAttribute("project", new Project());
        return "create-project";
    }

    @PostMapping("/create-project")
    public String saveProject(@ModelAttribute("project") Project project) {
        projectService.createProject(project);
        return "redirect:/read-projects";
    }

    @GetMapping("/create-subproject/{projectID}")
    public String createSubProject(@PathVariable("projectID") int projectID, Model model) {
        Project project = projectService.readProjectByID(projectID);
        model.addAttribute("project", project);
        model.addAttribute("subproject", new SubProject());
        return "create-subproject";
    }

    @PostMapping("/create-subproject/{projectID}/add")
    public String createSubProject(@PathVariable("projectID") int projectID, @ModelAttribute SubProject subProject) {
        subProject.setProjectID(projectID);
        subProjectService.createSubProject(subProject);
        return "redirect:/read-project/" + projectID;
    }

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/read-employees")
    public String readAllEmployees(Model model) {
        List<Employee> employees = empService.readAllEmployees();
        model.addAttribute("employees", employees);
        return "read-employees";
    }

    @GetMapping("/read-employee/{empId}")
    public String readEmployeeById(@PathVariable int empId, Model model) {
        Employee employee = empService.readEmployeeById(empId);
        model.addAttribute("employee", employee);
        return "read-employee";
    }

    @GetMapping("/pl/read-tasks")
    public String readAllTasks(Model model) {
        List<Task> tasks = taskService.readAllTasks();
        model.addAttribute("tasks", tasks);
        return "read-tasks";
    }

    @GetMapping("/read-tasks/{taskID}")
    public String readTaskByID(@PathVariable int taskID, Model model) {
        Task task = taskService.readTaskByID(taskID);
        model.addAttribute("task", task);
        return "read-task";
    }

    @GetMapping("/emp/{empID}/read-tasks")
    public String readMyTasks(@PathVariable int empID, Model model) {
        List<Task> myTasks = taskService.readMyTasks(empID);
        model.addAttribute("myTasks", myTasks);
        return "read-my-tasks";
    }

    @GetMapping("/read-projects")
    public String readAllprojects(Model model) {
        List<Project> projects = projectService.readAllProjects();
        model.addAttribute("projects", projects);
        return "read-projects";
    }

//    @GetMapping("/read-project/{projectID}")
//    public String getProjectByID(@PathVariable int projectID, Model model) {
//        Project project = projectService.readProjectByID(projectID);
//        model.addAttribute("project", project);
//        return "read-project";
//    }

    @GetMapping("/read-project/{projectID}")
    public String readProjectByIDWithTime(@PathVariable("projectID") int projectID, Model model) {
        Project project = projectService.readProjectByID(projectID);
        int totalEstimate = projectService.getTimeEstFromSubProjects(projectID);

        for (SubProject subProject : project.getSubProjects()) {
            int est = subProjectService.getTimeEstFromTasks(subProject.getSubProjectID());
            subProject.setTimeEst(est);


            model.addAttribute("project", project);
            model.addAttribute("timeEstimate", totalEstimate);
        }
        return "read-project";
    }

    @GetMapping("/read-subprojects")
    public String readAllSubProjects(Model model) {
        List<SubProject> subProjects = subProjectService.readAllSubProjects();
        model.addAttribute("subProject", subProjects);
        return "read-subprojects";
    }

//    @GetMapping("/read-subproject/{subProjectID}")
//    public String getSubProjectByID(@PathVariable int subProjectID, Model model) {
//        SubProject subProject = subProjectService.readSubProjectByID(subProjectID);
//        model.addAttribute("subProject", subProject);
//        return "read-subproject";
//    }

    @GetMapping("/read-subproject/{subProjectID}")
    public String readSubProjectByIDWithTime(@PathVariable("subProjectID") int subProjectID, Model model) {
        SubProject subProject = subProjectService.readSubProjectByID(subProjectID);
        int totalEstimate = subProjectService.getTimeEstFromTasks(subProjectID);

        model.addAttribute("subProject", subProject);
        model.addAttribute("timeEstimate", totalEstimate);
        return "read-subproject";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    //Mapping to edit employees data
    @GetMapping("/admin/edit-employee/{empId}")
    public String editEmployee(@PathVariable int empId, Model model) {
        Employee employee = empService.readEmployeeById(empId);
        model.addAttribute("employee", employee);
        model.addAttribute("roles", Role.values());
        return "update-employee";
    }

    //Mapping to UPDATE employees data
    @PostMapping("/admin/edit-employee/{empId}")
    public String updateEmployee(@PathVariable int empId, @ModelAttribute("employee") Employee employee) {
        // Konverter enum til en String
        String roleAsString = employee.getRole().name(); // 'ADMIN', 'USER', etc.
        // Sæt rollen som en String i Employee objektet
        employee.setRole(Role.valueOf(roleAsString)); // Konverter tilbage til enum, hvis nødvendigt

        empService.updateEmployee(employee);
        return "redirect:/read-employee/" + empId;
    }

    @GetMapping("/pl/edit-task/{taskID}")
    public String editTask(@PathVariable int taskID, Model model) {
        Task task = taskService.readTaskByID(taskID);
        model.addAttribute("task", task);
        return "update-task";
    }

    @PostMapping("/pl/edit-task/{taskID}")
    public String updateTask(@PathVariable int taskID, @ModelAttribute("task") Task task) {
        taskService.updateTask(task);
        return "redirect:/read-tasks/" + taskID;
    }

    @GetMapping("/edit-project/{projectID}")
    public String editProject(@PathVariable int projectID, Model model) {
        Project project = projectService.readProjectByID(projectID);
        model.addAttribute("project", project);
        return "update-project";
    }

    @PostMapping("/edit-project/{projectID}")
    public String updateProject(@PathVariable int projectID, @ModelAttribute("project") Project project) {
        projectService.updateProject(project);
        return "redirect:/read-project/" + projectID;
    }

    @GetMapping("/edit-subproject/{subProjectID}")
    public String editSubProject(@PathVariable int subProjectID, Model model) {
        SubProject subProject = subProjectService.readSubProjectByID(subProjectID);
        model.addAttribute("subProject", subProject);
        return "update-subproject";
    }

    @PostMapping("/edit-subproject/{subProjectID}")
    public String updateSubProject(@PathVariable int subProjectID,
                                   @ModelAttribute("subProject") SubProject subProject) {
        subProjectService.updateSubProject(subProject);
        return "redirect:/read-subproject/" + subProjectID;
    }


    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/pl/delete-task/{taskID}")
    public String deleteTask(@PathVariable int taskID) {
        Task task = taskService.readTaskByID(taskID);
        taskService.deleteTask(task);
        return "redirect:/pl/read-tasks";
    }

    @PostMapping("/admin/delete-employee/{empId}")
    public String deleteEmployee(@PathVariable int empId) {
        Employee employee = empService.readEmployeeById(empId);
        empService.deleteEmployee(employee);
        return "redirect:/admin";
    }

    @PostMapping("/delete-project/{projectID}")
    public String deleteProject(@PathVariable int projectID) {
        Project project = projectService.readProjectByID(projectID);
        projectService.deleteProject(project);
        return "redirect:/read-projects";
    }

    @PostMapping("/delete-subproject/{subProjectID}")
    public String deleteSubProject(@PathVariable int subProjectID) {
        SubProject subProject = subProjectService.readSubProjectByID(subProjectID);
        subProjectService.deleteSubProject(subProject.getSubProjectID());
        return "redirect:/read-subprojects";
    }
}
