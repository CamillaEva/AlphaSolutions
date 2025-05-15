package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.service.EmpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EmpController {
    private final EmpService empService;

    public EmpController(EmpService empService) {
        this.empService = empService;
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

    //_______________________________________________UPDATE_____________________________________________________________
    //Mapping to edit employees data
    @GetMapping("/admin/edit-employee/{empId}")
    public String editEmployee ( @PathVariable int empId, Model model){
        Employee employee = empService.readEmployeeById(empId);
        model.addAttribute("employee", employee);
        model.addAttribute("roles", Role.values());
        return "update-employee";
    }

    //Mapping to UPDATE employees data
    @PostMapping("/admin/edit-employee/{empId}")
    public String updateEmployee ( @PathVariable int empId, @ModelAttribute("employee") Employee employee){
        // Konverter enum til en String
        String roleAsString = employee.getRole().name(); // 'ADMIN', 'USER', etc.
        // Sæt rollen som en String i Employee objektet
        employee.setRole(Role.valueOf(roleAsString)); // Konverter tilbage til enum, hvis nødvendigt

        empService.updateEmployee(employee);
        return "redirect:/read-employee/" + empId;
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/admin/delete-employee/{empId}")
    public String deleteEmployee ( @PathVariable int empId){
        Employee employee = empService.readEmployeeById(empId);
        empService.deleteEmployee(employee);
        return "redirect:/admin";
    }
}
