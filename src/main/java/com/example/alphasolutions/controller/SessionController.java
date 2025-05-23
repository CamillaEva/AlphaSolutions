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
    private final EmpService empService;

    public SessionController(EmpService empService, ProjectService projectService) {
        this.empService = empService;
        this.projectService = projectService;
    }

    @GetMapping("/")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/main-page/{empID}")
    public String mainPage(@PathVariable int empID, HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");
        Employee sessionEmp = (Employee) session.getAttribute("emp");

        if (sessionEmp == null) {
            return "redirect:/";
        }


        if (sessionRole == Role.EMPLOYEE) {
            List<Project> myProjects = projectService.readMyProjects(empID);

            model.addAttribute("myProjects", myProjects);
            model.addAttribute("sessionEmp", sessionEmp);
            return "main-page";
        }
        if (sessionRole == Role.PROJECT_LEADER) {
            List<Project> allProjects = projectService.readAllProjects();
            model.addAttribute("allProjects", allProjects);
            model.addAttribute("sessionEmp", sessionEmp);
            return "main-page";
        }
        if (sessionRole == Role.ADMIN) {
            List<Employee> employees = empService.readAllEmployees();
            model.addAttribute("employees", employees);
            model.addAttribute("sessionEmp", sessionEmp);
            return "main-page";
        }

        return "error/no-access";
    }

    @PostMapping("/")
    public String login(@RequestParam("mailInitials") String mailInitials, @RequestParam("password") String password, HttpSession session, Model model) {
        String mail = mailInitials + "@alphasolutions.com";

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
        return "redirect:/";
    }
}
