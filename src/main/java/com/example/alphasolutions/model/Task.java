package com.example.alphasolutions.model;

import java.time.LocalDate;
import java.util.List;

public class Task {


    private int taskID;
    private int subProjectID;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int timeEst;
    private int usedTime;
    private List<Employee> employees;



    public Task(int taskID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst) {
        this.taskID = taskID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
    }

    public Task(int taskID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst, int subProjectID) {
        this.taskID = taskID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
        this.subProjectID = subProjectID;
    }

    //TODO: fiks om nogle af constructorene kan slås sammen?
    public Task(int taskID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst, int subProjectID, List<Employee> employees) {
        this.taskID = taskID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
        this.subProjectID = subProjectID;
        this.employees = employees;
    }

    //TODO Constructor when usedTime is being added
//    public Task(int taskID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst, int usedTime) {
//        this.taskID = taskID;
//        this.name = name;
//        this.description = description;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.timeEst = timeEst;
//        this.usedTime = usedTime;
//    }

    public Task() {

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

    //TODO When usedTime is being added to tasks
    public int getUsedTime() {
        return usedTime;
    }

    //TODO When usedTime is being added to tasks
    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
