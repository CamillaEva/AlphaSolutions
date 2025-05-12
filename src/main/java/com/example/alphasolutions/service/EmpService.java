package com.example.alphasolutions.service;


import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.repository.EmpRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpService {

    private final EmpRepository empRepository;

    public EmpService(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    //-----------------------------------ADMIN-----------------------------------------------------
    public boolean adminLogin(String mail, String password) {
        Employee employee = empRepository.getAdmin(mail);
        if (employee != null) {
            // Admin found - check credentials
            return employee.getPassword().equals(password);
        }
        return false;
    }

    //---------------------------------------LOGIN---------------------------------------------------
    public void attributeSetup(HttpSession session, Employee employee){
        empRepository.attributeSetup(session, employee);
    }

    //----------------------------------------CREATE--------------------------------------------------------------
    public int createEmployee(Employee employee) {
        return empRepository.createEmployee(employee);
    }

    //----------------------------------------READ--------------------------------------------------------------
    public List<Employee> getAllEmployees() {
        return empRepository.getAllEmployees();
    }

    public Employee readEmployeeById(int empId) {
        return empRepository.readEmployeeById(empId);
    }

    public Employee signIn(String mail, String password){
        return empRepository.signIn(mail, password);
    }

    //----------------------------------------UPDATE--------------------------------------------------------------
    public void updateEmployee(Employee employee) {
        empRepository.updateEmployee(employee);
    }

    //----------------------------------------DELETE--------------------------------------------------------------
    public void deleteEmployee(Employee employee) {
        empRepository.deleteEmployee(employee);
    }
}
