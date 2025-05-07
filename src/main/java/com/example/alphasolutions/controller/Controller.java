package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.service.EmpService;
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
    private final TaskService taskService;

    public Controller(EmpService empService, TaskService taskService) {
        this.empService = empService;
        this.taskService = taskService;
    }

    @GetMapping("/admin")
    public String adminMainPage() {
        return "admin-main"; //TODO: måske bare lav en html med if statements i forhold til hvad der skal være på main page
    }

    //____________________________________CREATE METHODS____________________________________
    @GetMapping("/admin/create-employee")
    public String createEmployee(Model model) {
        model.addAttribute("emp", new Employee());
        model.addAttribute("roles", Role.values());
        return "create-employee";
    }

    @PostMapping("/admin/create-employee")
    public String saveEmployee(@ModelAttribute("emp") Employee employee) {
        empService.createEmployee(employee);
        return "redirect:/admin/read-employees";
    }

    @GetMapping("/pl/create-task")
    public String createTask(Model model){
        model.addAttribute("task", new Task());
        return "create-task";
    }

    @PostMapping("/pl/create-task")
    public String saveTask(@ModelAttribute("task") Task task){
        taskService.createTask(task);
        return "redirect:/pl/read-tasks";
    }

    //____________________________________READ METHODS______________________________________
    @GetMapping("/admin/read-employees")
    public String readAllEmployees(Model model) {
        List<Employee> employees = empService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "read-employees";
    }

    @GetMapping("/admin/read-employee/{empId}")
    public String readEmployeeById(@PathVariable int empId, Model model) {
        Employee employee = empService.readEmployeeById(empId);
        model.addAttribute("employee", employee);
        return "read-employee";
    }

    @GetMapping("/pl/read-tasks")
    public String readAllTasks(Model model){
        List<Task> tasks = taskService.readAllTasks();
        model.addAttribute("tasks", tasks);
        return "read-tasks";
    }

    @GetMapping("/read-tasks/{taskID}")
    public String readTaskByID(@PathVariable int taskID, Model model){
        Task task = taskService.readTaskByID(taskID);
        model.addAttribute("task", task);
        return "read-task";
    }

    @GetMapping("/emp/{empID}/read-tasks")
        public String readMyTasks(@PathVariable int empID, Model model){
            List<Task> myTasks = taskService.readMyTasks(empID);
            model.addAttribute("myTasks", myTasks);
            return "read-my-tasks";
        }


    //____________________________________UPDATE METHODS____________________________________
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
        return "redirect:/admin/read-employee/" + empId;
    }

    @GetMapping("/pl/edit-task/{taskID}")
    public String editTask(@PathVariable int taskID, Model model){
        Task task = taskService.readTaskByID(taskID);
        model.addAttribute("task", task);
        return "update-task";
    }

    @PostMapping("/pl/edit-task/{taskID}")
    public String updateTask (@PathVariable int taskID, @ModelAttribute("task") Task task){
        taskService.updateTask(task);
        return "redirect:/read-tasks/" + taskID;
    }

    //____________________________________DELETE METHODS____________________________________
    @PostMapping("/pl/delete-task/{taskID}")
    public String deleteTask(@PathVariable int taskID){
        Task task = taskService.readTaskByID(taskID);
        taskService.deleteTask(task);
        return "redirect:/pl/read-tasks";
    }

    @PostMapping("/home/delete-employee/{empId}")
    public String deleteEmployee(@PathVariable int empId) {
        Employee employee = empService.readEmployeeById(empId);
        empService.deleteEmployee(employee);
        return "redirect:/home/read-employees";
    }

}
