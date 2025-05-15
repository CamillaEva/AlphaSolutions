package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.service.SubprojectService;
import com.example.alphasolutions.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final SubprojectService subprojectService;

    public TaskController(TaskService taskService, SubprojectService subprojectService) {
        this.taskService = taskService;
        this.subprojectService = subprojectService;
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
        model.addAttribute("task", task);
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
    public String editTask ( @PathVariable int taskID, Model model){
        Task task = taskService.readTaskByID(taskID);
        model.addAttribute("task", task);
        return "update-task";
    }

    @PostMapping("/pl/edit-task/{taskID}")
    public String updateTask ( @PathVariable int taskID, @ModelAttribute("task") Task task){
        taskService.updateTask(task);
        return "redirect:/read-tasks/" + taskID;
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/pl/delete-task/{taskID}")
    public String deleteTask ( @PathVariable int taskID){
        Task task = taskService.readTaskByID(taskID);
        taskService.deleteTask(task);
        return "redirect:/pl/read-tasks";
    }


}
