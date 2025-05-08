package com.example.alphasolutions.service;

import com.example.alphasolutions.model.SubProject;
import com.example.alphasolutions.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {


    private final SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository){
        this.subProjectRepository = subProjectRepository;
    }


    //___________________________CREATE______________________________________________

    public void createSubProject(SubProject subProject){
        subProjectRepository.createSubProject(subProject);
    }

    public void addSubProject(SubProject subProject){
        subProjectRepository.addSubProject(subProject);
    }

    //_____________________________READ________________________________________________

    public List<SubProject> readAllSubProjects(){
        return subProjectRepository.readAllSubProjects();
    }

    public List<SubProject> getSubProjectsByProjectID(int projectID){
        return subProjectRepository.getSubProjectsByProjectID(projectID);
    }

    public SubProject getSubProjectByID(int subProjectID){
        return subProjectRepository.getSubProjectById(subProjectID);
    }

    //_______________________________UPDATE______________________________________________

    public void updateSubProject(SubProject subProject){
        subProjectRepository.updateSubProject(subProject);
    }

    //______________________________DELETE_____________________________________________

    public void deleteSubProject(int id){
        subProjectRepository.deleteSubProject(id);
    }








}
