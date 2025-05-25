package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void assignEmpToTask() {
    }

    @Test
    void showAssignedEmpTask() {
    }

    @Test
    void createTask() {
       //arrange
        Task newTask = new Task(2, "Test task", "Description of test task", LocalDate.of(2025, 5, 24), LocalDate.of(2025, 5, 28), 10, 1,0);

        //act
        int newTaskID = taskRepository.createTask(newTask);

        //arrange
        int expected = 1;
        int actual = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TASK WHERE TASKID = ?",Integer.class, newTaskID);
        assertEquals(expected, actual);
    }

    @Test
    void readTaskByID() {
        //arrange
        int taskID = 1;

        //act
        Task task = taskRepository.readTaskByID(taskID);

        //assert
        assertNotNull(task);
        assertEquals(taskID, task.getTaskID());
        assertEquals("Task 1", task.getName());
        assertEquals("Første testopgave", task.getDescription());
        assertEquals(10, task.getTimeEst());
        assertEquals(1, task.getSubprojectID());
        assertEquals(3, task.getUsedTime());
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