package com.example.alphasolutions.controller;


import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Role;
import com.example.alphasolutions.service.EmpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SessionController {
    private EmpService empService;

    public SessionController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminMainPage(Model model) {
        List<Employee> employee = empService.readAllEmployees();
        model.addAttribute("emp", employee);
        return "admin-main"; //TODO: måske bare lav en html med if statements i forhold til hvad der skal være på main page
    }

    @GetMapping("/main-page/{empID}")
    public String showMainPage(){
        return "main-page";
    }

    @PostMapping("/")
    public String login(@RequestParam("mail") String mail, @RequestParam("password") String password, HttpSession session, Model model) {
        // checking if it's an admin user
        if (empService.adminLogin(mail, password)) {
            Employee admin = new Employee(mail, password, Role.ADMIN);
           empService.attributeSetup(session,admin);
            return "redirect:/admin";
        }

        // if the mail and password doesn't match the admin account, then check for employees
        Employee employee = empService.signIn(mail, password);

        if (employee != null) {
            empService.attributeSetup(session,employee);
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
