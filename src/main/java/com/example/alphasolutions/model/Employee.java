package com.example.alphasolutions.model;

import java.util.List;

public class Employee {

    private int empID;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private Role role;
    private List<Task> tasks;


    public Employee(int empID, String firstName, String lastName, String mail, String password, Role role) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }


    public Employee() {

    }


    public int getEmpID() {
        return empID;
    }


    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


}
