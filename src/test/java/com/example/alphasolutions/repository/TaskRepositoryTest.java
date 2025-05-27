package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Subproject;
import com.example.alphasolutions.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

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
        int empID = 3;
        int taskID = 1;

        taskRepository.assignEmpToTask(taskID, empID);

        String sql = "SELECT COUNT(*) FROM EMP_TASK WHERE EMPID = ? AND TASKID = ?";
        Integer actual = jdbcTemplate.queryForObject(sql, Integer.class, empID, taskID);

        assertEquals(1, actual);
    }

    @Test
    void showAssignedEmpTask() {
        int taskID = 1;
        List<Integer> expected = List.of(1);

        List<Integer> actual = taskRepository.showAssignedEmpTask(taskID);

        assertEquals(expected, actual);
    }

    @Test
    void createTask() {
        //arrange
        Task newTask = new Task(2, "Test task", "Description of test task", LocalDate.of(2025, 5, 24), LocalDate.of(2025, 5, 28), 10, 1, 0);

        //act
        int newTaskID = taskRepository.createTask(newTask);

        //arrange
        int expected = 1;
        int actual = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TASK WHERE TASKID = ?", Integer.class, newTaskID);
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
        int subprojectID = 1;
        int empID = 3;

        taskRepository.assignEmpToTask(1, empID);
        List<Task> myTasks = taskRepository.readMyTasks(empID, subprojectID);

        assertEquals(1, myTasks.size());

        Task task = myTasks.get(0);
        assertEquals(1, task.getTaskID());
        assertEquals("Task 1", task.getName());
        assertEquals("Første testopgave", task.getDescription());
    }

    @Disabled
    void readTotalTimeEstimateForProject() {
    }

    @Disabled
    void readTotalUsedTimeForProject() {
    }

    @Test
    void updateTask() {
        Task updatedTask = new Task(1, "nyopdateret test", "ændrede informationer", LocalDate.of(2025, 5, 14), LocalDate.of(2025, 5, 28), 10, 1, 2);

        taskRepository.updateTask(updatedTask);

        Task actualNewTask = taskRepository.readTaskByID(1);

        assertEquals(1, actualNewTask.getTaskID());
        assertEquals("nyopdateret test", actualNewTask.getName());
        assertEquals("ændrede informationer", actualNewTask.getDescription());
        assertEquals(LocalDate.of(2025, 5, 14), actualNewTask.getStartDate());
        assertEquals(LocalDate.of(2025, 5, 28), actualNewTask.getEndDate());
        assertEquals(10, actualNewTask.getTimeEst());
    }

    @Disabled
    void updateUsedTime() {
    }

    @Test
    void deleteTask() {
        int taskID = 1;

        Integer task = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TASK WHERE TASKID = ?", Integer.class, taskID);
        Integer empTask = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM EMP_TASK WHERE TASKID = ?", Integer.class, taskID);
        Integer subprojectTask = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SUBPROJECT_TASKS WHERE TASKID = ?", Integer.class, taskID);

        assertEquals(1, task);
        assertEquals(1, empTask);
        assertEquals(1, subprojectTask);

        Task taskToDelete = new Task();
        taskToDelete.setTaskID(taskID);
        taskRepository.deleteTask(taskToDelete);

        Integer afterDeleteTask = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TASK WHERE TASKID = ?", Integer.class, taskID);
        Integer afterDeleteEmpTask = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM EMP_TASK WHERE TASKID = ?", Integer.class, taskID);
        Integer afterDeleteSubprojectTask = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM SUBPROJECT_TASKS WHERE TASKID = ?", Integer.class, taskID);

        assertEquals(0, afterDeleteTask);
        assertEquals(0, afterDeleteEmpTask);
        assertEquals(0, afterDeleteSubprojectTask);
    }
}