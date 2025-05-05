package com.example.alphasolutions.service;

import com.example.alphasolutions.model.Employee;
import com.example.alphasolutions.model.Task;
import com.example.alphasolutions.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }


    //----------------------------------------CREATE--------------------------------------------------------------
    public int createTask(Task task){
       return taskRepository.createTask(task);
    }

    //----------------------------------------READ----------------------------------------------------------------
    public List<Task> readAllTasks(){
        return taskRepository.readAllTask();
    }

    public Task readTaskByID(int taskID){
        return taskRepository.readTaskByID(taskID);
    }

    public List<Task> readMyTasks(int empID){
        return taskRepository.readMyTasks(empID);
    }

    //----------------------------------------UPDATE----------------------------------------------------------------
    public void updateTask(Task task){
        taskRepository.updateTask(task);
    }

    //----------------------------------------DELETE----------------------------------------------------------------
    public void deleteTask(Task task){
        taskRepository.deleteTask(task);
    }

}
