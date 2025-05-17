package com.example.alphasolutions.controller;


import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SessionController {
    private final ProjectService projectService;
    private EmpService empService;

    public SessionController(EmpService empService, ProjectService projectService) {
        this.empService = empService;
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminMainPage(Model model, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.ADMIN) {
            List<Employee> employee = empService.readAllEmployees();
            model.addAttribute("emp", employee);
            return "admin-main"; //TODO: måske bare lav en html med if statements i forhold til hvad der skal være på main page
        }
        return "error/no-access";
    }

    @GetMapping("/main-page/{empID}")
    public String showMainPage(@PathVariable int empID, HttpSession session, Model model) {
        List<Project> projects = projectService.readAllProjects();
        model.addAttribute("projects", projects);

        Employee sessionEmp = (Employee) session.getAttribute("emp");
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionEmp == null || sessionEmp.getEmpID() != empID) {
            return "redirect:/";
        }

        if (sessionRole == Role.PROJECT_LEADER || sessionRole == Role.EMPLOYEE) {
            return "main-page";
        }
        return "error/no-access";
    }

    @PostMapping("/")
    public String login(@RequestParam("mail") String mail, @RequestParam("password") String password, HttpSession session, Model model) {
        // checking if it's an admin user
        if (empService.adminLogin(mail, password)) {
            Employee admin = new Employee(mail, password, Role.ADMIN);
            empService.attributeSetup(session, admin);
            return "redirect:/admin";
        }

        // if the mail and password doesn't match the admin account, then check for employees
        Employee employee = empService.signIn(mail, password);

        if (employee != null) {
            empService.attributeSetup(session, employee);
            return "redirect:/main-page/" + employee.getEmpID();
        }

        model.addAttribute("wrongCredentials", true);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
