package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.Admin;
import com.example.alphasolutions.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SessionController {
    private AdminService adminService;

    public SessionController(AdminService adminService) {
        this.adminService = adminService;
    }

    //maybe delete this method??????????
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping("/home/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/home/login")
    public String login(@RequestParam("uid") String uid, @RequestParam("pw") String pw, HttpSession session, Model model) {
        if (adminService.adminLogin(uid, pw)) {
            session.setAttribute("admin", new Admin(uid, pw));
            session.setMaxInactiveInterval(30); //Session timout 30sec, does not redirect before a request is sent.
            return "redirect:/home";
        }
        model.addAttribute("wrongCredentials", true);
        return "login";
    }

    @GetMapping("/home/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home/login";
    }
}
