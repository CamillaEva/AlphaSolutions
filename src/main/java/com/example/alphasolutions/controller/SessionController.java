package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Admin;
import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.service.AdminService;
import com.example.alphasolutions.service.EmpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SessionController {
    private AdminService adminService;
    private EmpService empService;

    public SessionController(AdminService adminService, EmpService empService) {
        this.adminService = adminService;
        this.empService = empService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminMainPage() {
        return "admin-main"; //TODO: måske bare lav en html med if statements i forhold til hvad der skal være på main page
    }

    @GetMapping("/main-page/{empID}")
    public String showMainPage(){
        return "main-page";
    }

    @PostMapping("/login")
    public String login(@RequestParam("mail") String mail, @RequestParam("password") String password, HttpSession session, Model model) {
        Employee employee = empService.signIn(mail, password);
        if (adminService.adminLogin(mail,password)) {
            session.setAttribute("admin", new Admin(mail, password));
            session.setMaxInactiveInterval(30); //Session timout 30sec, does not redirect before a request is sent.
            return "redirect:/admin";
        } else if (employee != null) {
           session.setAttribute("emp", employee);
           session.setAttribute("role", employee.getRole());
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
