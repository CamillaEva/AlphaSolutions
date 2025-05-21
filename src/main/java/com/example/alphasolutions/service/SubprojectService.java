package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.repository.SubprojectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubprojectService {


    private final SubprojectRepository subProjectRepository;
    private final SubprojectRepository subprojectRepository;

    public SubprojectService(SubprojectRepository subProjectRepository, SubprojectRepository subprojectRepository) {
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
    public int createSubProject(Subproject subProject) {
        return subProjectRepository.createSubProject(subProject);
    } //Change to createSubProject + change in controller

    //_______________________________________________READ_______________________________________________________________
    //TODO Not used
    public List<Subproject> getSubProjectsByProjectID(int projectID) {
        return subProjectRepository.getSubProjectsByProjectID(projectID);
    }

    public Subproject readSubProjectByID(int subProjectID) {
        return subProjectRepository.readSubProjectById(subProjectID);
    }

    public int getTimeEstFromTasks(int subProjectID) {
        return subProjectRepository.getTimeEstFromTasks(subProjectID);
    }

    //TEST
    public int readTotalTimeEstimateForProject(int projectID) {
        return subProjectRepository.readTotalTimeEstimateForProject(projectID);
    }

    public int readTotalUsedTimeForProject(int projectID) {
        return subProjectRepository.readTotalUsedTimeForProject(projectID);
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
