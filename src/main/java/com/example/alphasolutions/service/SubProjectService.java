package com.example.alphasolutions.service;

import com.example.alphasolutions.model.SubProject;
import com.example.alphasolutions.repository.SubprojectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {


    private final SubprojectRepository subProjectRepository;
    private final SubprojectRepository subprojectRepository;

    public SubProjectService(SubprojectRepository subProjectRepository, SubprojectRepository subprojectRepository) {
        this.subProjectRepository = subProjectRepository;
        this.subprojectRepository = subprojectRepository;
    }

    //___________________________________________ASSIGN EMP________________________________________________________________
    public void assignTaskToSubproject(int taskID, int subprojectID) {
        subprojectRepository.assignTaskToSubproject(taskID, subprojectID);
    }

    public List<Integer> showAssignedEmpSubproject(int subprojectID) {
        return subProjectRepository.showAssignedEmpSubproject(subprojectID);
    }



    //_______________________________________________CREATE_____________________________________________________________
    public int createSubProject(SubProject subProject) {
        return subProjectRepository.createSubProject(subProject);
    } //Change to createSubProject + change in controller

    //_______________________________________________READ_______________________________________________________________
    public SubProject readSubProjectByID(int subProjectID) {
        return subProjectRepository.readSubProjectById(subProjectID);
    }

    public int getTimeEstFromTasks(int subProjectID) {
        return subProjectRepository.getTimeEstFromTasks(subProjectID);
    }

    public List<SubProject> readMySubprojects(int empID, int projectID) {
        return subProjectRepository.readMySubprojects(projectID, empID);
    }

    public List<SubProject> getSubProjectsByProjectID(int projectID) {
        return subProjectRepository.readSubProjectsByProjectID(projectID);
    }

    //TEST
    public int readTotalTimeEstimateForProject(int projectID) {
        return subProjectRepository.readTotalTimeEstimateForProject(projectID);
    }

    public int readTotalUsedTimeForProject(int projectID) {
        return subProjectRepository.readTotalUsedTimeForProject(projectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateSubProject(SubProject subProject) {
        subProjectRepository.updateSubProject(subProject);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteSubProject(SubProject subproject) {
        subProjectRepository.deleteSubProject(subproject);
    }


}
