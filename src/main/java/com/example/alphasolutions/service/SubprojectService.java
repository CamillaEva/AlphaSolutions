package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.repository.SubprojectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubprojectService {

    private final SubprojectRepository subprojectRepository;

    public SubprojectService(SubprojectRepository subprojectRepository) {
        this.subprojectRepository = subprojectRepository;
    }

    //___________________________________________ASSIGN EMP________________________________________________________________
    public void assignTaskToSubproject(int taskID, int subprojectID) {
        subprojectRepository.assignTaskToSubproject(taskID, subprojectID);
    }

    public List<Integer> showAssignedEmpSubproject(int subprojectID) {
        return subprojectRepository.showAssignedEmpSubproject(subprojectID);
    }

    //_______________________________________________CREATE_____________________________________________________________
    public int createSubproject(Subproject subproject) {
        return subprojectRepository.createSubProject(subproject);
    }

    //_______________________________________________READ_______________________________________________________________
    public Subproject readSubprojectByID(int subprojectID) {
        return subprojectRepository.readSubprojectById(subprojectID);
    }

    public int readTimeEstFromTasks(int subprojectID) {
        return subprojectRepository.readTimeEstFromTasks(subprojectID);
    }

    public List<Subproject> readMySubprojects(int empID, int projectID) {
        return subprojectRepository.readMySubprojects(projectID, empID);
    }

    public List<Subproject> readSubprojectsByProjectID(int projectID) {
        return subprojectRepository.readSubprojectsByProjectID(projectID);
    }

    public int readTotalTimeEstimateForProject(int projectID) {
        return subprojectRepository.readTotalTimeEstimateForProject(projectID);
    }

    public int readTotalUsedTimeForProject(int projectID) {
        return subprojectRepository.readTotalUsedTimeForProject(projectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateSubproject(Subproject subproject) {
        subprojectRepository.updateSubproject(subproject);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteSubproject(Subproject subproject) {
        subprojectRepository.deleteSubproject(subproject);
    }
}
