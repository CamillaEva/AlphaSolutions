package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {


    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }


    //_______________________CREATE_____________________________________________________________________________________

    public int createProject(Project project){
        return projectRepository.createProject(project);

    }

    //_______________________READ_______________________________________________________________________________________

    public List<Project> readAllProjects(){
        return projectRepository.readAllProjects();
    }

    public Project getProjectByID(int projectID){
        return projectRepository.getProjectByID(projectID);
    }

    //_______________________UPDATE_____________________________________________________________________________________

    public void updateProject(Project project){
        projectRepository.updateProject(project);
    }

    //_______________________DELETE_____________________________________________________________________________________

    public void deleteProject(Project project){
        projectRepository.deleteProject(project);
    }

    //_______________________________________________________________________________




}
