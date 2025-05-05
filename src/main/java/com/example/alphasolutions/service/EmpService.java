package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.repository.EmpRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpService {

    private final EmpRepository empRepository;

    public EmpService(EmpRepository empRepository) {
        this.empRepository = empRepository;
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

    //----------------------------------------UPDATE--------------------------------------------------------------
    public void updateEmployee(Employee employee) {
        empRepository.updateEmployee(employee);
    }

    //----------------------------------------DELETE--------------------------------------------------------------
    public void deleteEmployee(Employee employee) {
        empRepository.deleteEmployee(employee);
    }
}
