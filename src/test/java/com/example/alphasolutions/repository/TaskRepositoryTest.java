package com.example.alphasolutions.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void assignEmpToTask() {
    }

    @Test
    void showAssignedEmpTask() {
    }

    @Test
    void createTask() {
    }

    @Test
    void readAllTask() {
    }

    @Test
    void readTaskByID() {
    }

    @Test
    void readMyTasks() {
    }

    @Test
    void readTotalTimeEstimateForProject() {
    }

    @Test
    void readTotalUsedTimeForProject() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateUsedTime() {
    }

    @Test
    void deleteTask() {
    }
}