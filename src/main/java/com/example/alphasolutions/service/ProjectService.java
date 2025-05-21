package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Project;
import com.example.alphasolutions.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {


    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    //______________________________________________ASSIGN EMP__________________________________________________________
    public void assignSubprojectToProject(int subprojectID, int projectID) {
        projectRepository.assignSubprojectToProject(subprojectID, projectID);
    }

    public List<Integer> showAssignedEmpProject(int projectID) {
        return projectRepository.showAssignedEmpProject(projectID);
    }


    //_______________________________________________CREATE_____________________________________________________________
    public int createProject(Project project) {
        return projectRepository.createProject(project);
    }

    //_______________________________________________READ_______________________________________________________________
    public List<Project> readAllProjects() {
        return projectRepository.readAllProjects();
    }

    public Project readProjectByID(int projectID) {
        return projectRepository.readProjectByID(projectID);
    }

    public int readTotalTimeEstimateForProject(int projectID) {
        return projectRepository.readTotalTimeEstimateForProject(projectID);
    }

    public int readTotalUsedTimeForProject(int projectID) {
        return projectRepository.readTotalUsedTimeForProject(projectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteProject(Project project) {
        projectRepository.deleteProject(project);
    }


}
