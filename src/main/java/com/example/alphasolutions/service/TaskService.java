package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //___________________________________________ASSIGN EMP________________________________________________________________
    public void assignEmpToTask(int taskID, int empID) {
        taskRepository.assignEmpToTask(taskID, empID);
    }

    public List<Integer> showAssignedEmpTask(int taskID) {
        return taskRepository.showAssignedEmpTask(taskID);
    }

    //_______________________________________________CREATE_____________________________________________________________
    public int createTask(Task task) {
        return taskRepository.createTask(task);
    }

    //_______________________________________________READ_______________________________________________________________
    public Task readTaskByID(int taskID) {
        return taskRepository.readTaskByID(taskID);
    }

    public List<Task> readMyTasks(int empID, int subprojectID) {
        return taskRepository.readMyTasks(empID, subprojectID);
    }

    public int readTotalTimeEstimateForProject(int projectID) {
        return taskRepository.readTotalTimeEstimateForProject(projectID);
    }

    public int readTotalUsedTimeForProject(int projectID) {
        return taskRepository.readTotalUsedTimeForProject(projectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }

    public void updateUsedTime(Task task) {
        taskRepository.updateUsedTime(task);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }
}
