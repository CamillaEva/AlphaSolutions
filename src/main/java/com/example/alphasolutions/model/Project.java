package com.example.alphasolutions.model;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class Project {

    private int projectID;
    private int subprojectID;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int timeEst;
    private int usedTime; //har egen constructor - nice-to-have
    //private Status status; //opret enum klasse  //har egen constructor - nice-to-have


    public Project(int projectID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst, int usedTime) {
        this.projectID = projectID;
        this.subprojectID = subprojectID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
        this.usedTime = usedTime;
    }

    public Project(int projectID, int subprojectID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst) {
        this.projectID = projectID;
        this.subprojectID = subprojectID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeEst = timeEst;
        this.usedTime = usedTime;
        this.status = status;
    }

// constructor, if we need to work with status on a project!
//    public Project(int projectID, int subprojectID, String name, String description, LocalDate startDate, LocalDate endDate, int timeEst, int usedTime, Status status) {
//        this.projectID = projectID;
//        this.subprojectID = subprojectID;
//        this.name = name;
//        this.description = description;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.timeEst = timeEst;
//        this.usedTime = usedTime;
//        this.status = status;
//    }


    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getSubprojectID() {
        return subprojectID;
    }

    public void setSubprojectID(int subprojectID) {
        this.subprojectID = subprojectID;
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
