package com.example.alphasolutions.model;

import java.time.LocalDate;
import java.util.List;

public class Project {

    private int projectID;
    private List<Subproject> subprojects;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;


    public Project() {

    }

    public Project(int projectID, String name, String description, LocalDate startDate, LocalDate endDate) {
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void createSubproject(Subproject subproject) {
        subprojects.add(subproject);
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public List<Subproject> getSubprojects() {
        return subprojects;
    }

    public void setSubprojects(List<Subproject> subprojects) {
        this.subprojects = subprojects;
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
}
