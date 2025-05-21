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
    public void assignEmpToTask(int taskID, int empID){
        taskRepository.assignEmpToTask(taskID, empID);
    }

    public List<Integer> showAssignedEmpTask(int taskID){
        return taskRepository.showAssignedEmpTask(taskID);
    }


    //_______________________________________________CREATE_____________________________________________________________
    public int createTask(Task task) {
        return taskRepository.createTask(task);
    }

    //_______________________________________________READ_______________________________________________________________
    public List<Task> readAllTasks() {
        return taskRepository.readAllTask();
    }

    public Task readTaskByID(int taskID) {
        return taskRepository.readTaskByID(taskID);
    }

    public List<Task> readMyTasks(int empID, int subprojectID) {
        return taskRepository.readMyTasks(empID, subprojectID);
    }

    //_______________________________________________UPDATE_____________________________________________________________
    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }

    //_______________________________________________DELETE_____________________________________________________________
    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

}
