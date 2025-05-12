package com.example.alphasolutions.service;

import com.example.alphasolutions.model.SubProject;
import com.example.alphasolutions.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {


    private final SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }


    //_______________________________________________CREATE_____________________________________________________________
    public int createSubProject(SubProject subProject) {
        return subProjectRepository.createSubProject(subProject);
    } //Change to createSubProject + change in controller

    //_______________________________________________READ_______________________________________________________________
    public List<SubProject> readAllSubProjects() {
        return subProjectRepository.readAllSubProjects();
    }

    //TODO Not used
    public List<SubProject> getSubProjectsByProjectID(int projectID) {
        return subProjectRepository.getSubProjectsByProjectID(projectID);
    }

    public SubProject readSubProjectByID(int subProjectID) {
        return subProjectRepository.readSubProjectById(subProjectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateSubProject(SubProject subProject) {
        subProjectRepository.updateSubProject(subProject);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteSubProject(int id) {
        subProjectRepository.deleteSubProject(id);
    }


}
