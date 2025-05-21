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

    //---------------------------------------LOGIN---------------------------------------------------
    public void attributeSetup(HttpSession session, Employee employee){
        empRepository.attributeSetup(session, employee);
    }

    //_______________________________________________CREATE_____________________________________________________________
    public int createEmployee(Employee employee) {
        return empRepository.createEmployee(employee);
    }

    //_______________________________________________READ_______________________________________________________________
    public List<Employee> readAllEmployees() {
        return empRepository.readAllEmployees();
    }

    public Employee readEmployeeById(int empId) {
        return empRepository.readEmployeeById(empId);
    }

    public Employee signIn(String mail, String password){
        return empRepository.signIn(mail, password);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateEmployee(Employee employee) {
        empRepository.updateEmployee(employee);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteEmployee(Employee employee) {
        empRepository.deleteEmployee(employee);
    }
}
