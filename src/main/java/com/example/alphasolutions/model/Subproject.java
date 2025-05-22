package com.example.alphasolutions.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Subproject {


    private int subProjectID;
    private int projectID;
    private List<Task> tasks = new ArrayList<>();
    private int taskID;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int timeEst = 0;


    public Subproject() {
        tasks = new ArrayList<>();
    }

    public Subproject(int subProjectID, String name, String description, LocalDate startDate, LocalDate endDate) {
        this.subProjectID = subProjectID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Subproject(int subProjectID, int projectID, String name, String description, LocalDate startDate, LocalDate endDate) {
        this.subProjectID = subProjectID;
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Subproject(int subProjectID, int projectID, int taskID, String name, String description, LocalDate startDate, LocalDate endDate) {
        this.subProjectID = subProjectID;
        this.projectID = projectID;
        this.taskID = taskID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void createTask(Task task) {
        tasks.add(task);
    }


    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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
}
