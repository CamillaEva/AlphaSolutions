package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.service.EmpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmpController {
    private final EmpService empService;

    public EmpController(EmpService empService) {
        this.empService = empService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/create-employee")
    public String createEmployee(Model model, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        int sessionID = (int) session.getAttribute("id");

        if (sessionRole == Role.ADMIN) {
            model.addAttribute("emp", new Employee());
            model.addAttribute("roles", Role.values());
            model.addAttribute("sessionID", sessionID);
            return "create-employee";
        }
        return "error/no-access";
    }

    @PostMapping("/create-employee")
    public String saveEmployee(@RequestParam String mailInitials, @ModelAttribute("emp") Employee employee,
                               HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        int sessionID = (int) session.getAttribute("id");

        if (sessionRole == Role.ADMIN) {
            employee.setMail(mailInitials + "@alphasolutions.com");
            empService.createEmployee(employee);
            return "redirect:/main-page/" + sessionID;
        }
        return "error/no-access";
    }

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/read-employees")
    public String readAllEmployees(Model model, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        //TODO: alm. medarbejdere skal vel ikke kunne se alle medarbejdere, kun de tilknyttede til samme projekter osv?
        if (sessionRole == Role.PROJECT_LEADER || sessionRole == Role.ADMIN) {
            List<Employee> employees = empService.readAllEmployees();
            model.addAttribute("employees", employees);
            return "read-employees";
        }
        return "error/no-access";
    }

    @GetMapping("/read-employee/{empId}")
    public String readEmployeeById(@PathVariable int empId, Model model,
                                   HttpSession session) {

        Role sessionRole = (Role) session.getAttribute("role");
        int sessionID = (int) session.getAttribute("id");

        if (sessionRole == Role.PROJECT_LEADER || sessionRole == Role.ADMIN) {
            Employee employee = empService.readEmployeeById(empId);
            model.addAttribute("sessionID", sessionID);
            model.addAttribute("employee", employee);
            return "read-employee";
        }
        return "error/no-access";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/edit-employee/{empId}")
    public String editEmployee(@PathVariable int empId, Model model,
                               HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.ADMIN) {
            Employee employee = empService.readEmployeeById(empId);
            model.addAttribute("employee", employee);
            model.addAttribute("roles", Role.values());
            return "update-employee";
        }
        return "error/no-access";
    }

    @PostMapping("/edit-employee/{empId}")
    public String updateEmployee(@PathVariable int empId, @ModelAttribute("employee") Employee employee,
                                 HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.ADMIN) {

            String roleAsString = employee.getRole().name();

            employee.setRole(Role.valueOf(roleAsString)); // Covert back to enum if necessary
            empService.updateEmployee(employee);
            return "redirect:/read-employee/" + empId;
        }
        return "error/no-access";
    }

    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-employee/{empId}")
    public String deleteEmployee(@PathVariable int empId, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        int sessionID = (int) session.getAttribute("id");

        if (sessionRole == Role.ADMIN) {
            Employee employee = empService.readEmployeeById(empId);
            empService.deleteEmployee(employee);
            return "redirect:/main-page/" + sessionID;
        }
        return "error/no-access";
    }
}
