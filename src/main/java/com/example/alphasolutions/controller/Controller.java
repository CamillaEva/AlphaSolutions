package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.service.EmpService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private final EmpService empService;

    public Controller(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/home")
    public String homePage() {
        return "home"; // Dette refererer til home.html
    }


    //____________________________________CREATE METHODS____________________________________
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

    //____________________________________READ METHODS______________________________________
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


    //____________________________________UPDATE METHODS____________________________________
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

    //____________________________________DELETE METHODS____________________________________

}
