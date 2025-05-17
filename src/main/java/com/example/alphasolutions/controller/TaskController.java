package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import com.example.alphasolutions.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final SubprojectService subprojectService;
    private final ProjectService projectService;
    private final EmpService empService;

    public TaskController(TaskService taskService, SubprojectService subprojectService,
                          ProjectService projectService, EmpService empService) {
        this.taskService = taskService;
        this.subprojectService = subprojectService;
        this.projectService = projectService;
        this.empService = empService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/pl/create-task/{subProjectID}")
    public String createTask(@PathVariable("subProjectID") int subProjectID, Model model) {
        Subproject subProject = subprojectService.readSubProjectByID(subProjectID);
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

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/pl/read-tasks")
    public String readAllTasks(Model model) {
        List<Task> tasks = taskService.readAllTasks();
        model.addAttribute("tasks", tasks);
        return "read-tasks";
    }

    @GetMapping("/read-tasks/{taskID}")
    public String readTaskByID(@PathVariable int taskID, Model model) {
        Task task = taskService.readTaskByID(taskID);
        Subproject subproject = subprojectService.readSubProjectByID(task.getSubProjectID());
        Project project = projectService.readProjectByID(subproject.getProjectID());

        int totalTimeEstimate = 0;
        for (Subproject sp : project.getSubProjects()) {
            int est = subprojectService.getTimeEstFromTasks(sp.getSubProjectID());
            totalTimeEstimate += est;
        }
        model.addAttribute("task", task);
        model.addAttribute("totalTimeEstimate", totalTimeEstimate);
        return "read-task";
    }

    @GetMapping("/emp/{empID}/read-tasks")
    public String readMyTasks(@PathVariable int empID, Model model) {
        List<Task> myTasks = taskService.readMyTasks(empID);
        model.addAttribute("myTasks", myTasks);
        return "read-my-tasks";
    }

    //_______________________________________________UPDATE_____________________________________________________________
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

    //_______________________________________________DELETE_____________________________________________________________
    //_____________________________________________ATTACH_______________________________________________________________
    @GetMapping("/read-tasks/{taskID}/attach-emp")
    public String attachEmpToTask (@PathVariable int taskID, Model model){
        List<Employee> employees = empService.readAllEmployees();
        model.addAttribute("taskID", taskID);
        model.addAttribute("employees", employees);
        return "attach-emp";
    }

    @PostMapping("/read-tasks/{taskID}/attach-emp")
    public String attachEmpToTask (@PathVariable int taskID, @RequestParam("empSelected") List<Integer> selectedEmpIDs){

        if(selectedEmpIDs != null) {
            for(int empID : selectedEmpIDs){
                taskService.attachEmpToTask(taskID,empID);
            }
        }
        return "redirect:/read-tasks/" + taskID;
    }



}
