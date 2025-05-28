package com.example.alphasolutions.model;

import java.time.LocalDate;
import java.util.List;

public class Task {

    private int taskID;
    private int subprojectID;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int timeEst = 0;
    private int usedTime = 0;
    private List<Employee> employees;

    public Task() {

    }

    public Task(int taskID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst, int subprojectID, int usedTime) {
        this.taskID = taskID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
        this.subprojectID = subprojectID;
        this.usedTime = usedTime;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getTimeEst() {
        return timeEst;
    }

    public void setTimeEst(int timeEst) {
        this.timeEst = timeEst;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime){
        this.usedTime = usedTime;
    }

    public int getSubprojectID() {
        return subprojectID;
    }

    public void setSubprojectID(int subprojectID) {
        this.subprojectID = subprojectID;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
