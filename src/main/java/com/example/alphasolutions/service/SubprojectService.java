package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.repository.SubprojectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubprojectService {


    private final SubprojectRepository subProjectRepository;

    public SubprojectService(SubprojectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }


    //_______________________________________________CREATE_____________________________________________________________
    public int createSubProject(Subproject subProject) {
        return subProjectRepository.createSubProject(subProject);
    } //Change to createSubProject + change in controller

    //_______________________________________________READ_______________________________________________________________
    public List<Subproject> readAllSubProjects() {
        return subProjectRepository.readAllSubProjects();
    }

    //TODO Not used
    public List<Subproject> getSubProjectsByProjectID(int projectID) {
        return subProjectRepository.getSubProjectsByProjectID(projectID);
    }

    public Subproject readSubProjectByID(int subProjectID) {
        return subProjectRepository.readSubProjectById(subProjectID);
    }

    public int getTimeEstFromTasks(int subProjectID){
        return subProjectRepository.getTimeEstFromTasks(subProjectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateSubProject(Subproject subProject) {
        subProjectRepository.updateSubProject(subProject);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteSubProject(int id) {
        subProjectRepository.deleteSubProject(id);
    }


}
