package com.example.alphasolutions.model;

import java.time.LocalDate;
import java.util.List;

public class SubProject {


    private int subProjectID;
    private int projectID;
    private List<Task> tasks;
    private int taskID;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int timeEst;
    private int usedTime;  //har egen constructor - nice-to-have
//    private Status status; //har egen constructor - nice-to-have


    //constructor for if we need status in out project
//    public SubProject(int subProjectID, int taskID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst, int usedTime, Status status) {
//        this.subProjectID = subProjectID;
//        this.taskID = taskID;
//        this.name = name;
//        this.description = description;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.timeEst = timeEst;
//        this.usedTime = usedTime;
//        this.status = status;
//    }

    public SubProject(){

    }

    public SubProject(int subProjectID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst) {
        this.subProjectID = subProjectID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
    }

    public SubProject(int subProjectID, int projectID, int taskID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst) {
        this.subProjectID = subProjectID;
        this.projectID = projectID;
        this.taskID = taskID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
    }

    public void addTask(Task task){
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

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
}
