package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Admin;
import com.example.alphasolutions.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean adminLogin(String uid, String pw) {
        Admin admin = adminRepository.getAdmin(uid);
        if (admin != null) {
            // Admin found - check credentials
            return admin.getPw().equals(pw);
        }
        return false;
    }
}
