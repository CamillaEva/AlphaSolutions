package com.example.alphasolutions.repository;

import com.example.alphasolutions.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest

@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;



    @Test
    void createProject() {
        //Arrange
        Project project = new Project(2, "project number 3", "this is the 3rd project", LocalDate.of(2025, 5, 25), LocalDate.of(2025, 5, 28));
        //Act
        projectRepository.createProject(project);

        int expected = 2;
        int actual = projectRepository.readAllProjects().size();
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    void readAllProjects() {
        //Arrange

        //act
        int expected = 1;
        int actual = projectRepository.readAllProjects().size();

        //Assert
        assertEquals(expected, actual);
    }

    //this is still here, because we wrote about it in our paper.
//    @Test
//    void readProjectByID(){
//        //Arrange
//
//        //Act
//        Project project = projectRepository.readProjectByID(1);
//        //Assert
//        assertNotNull(project);
//        assertEquals("Testprojekt", project.getName());
//        assertEquals("Et projekt til integrationstest", project.getDescription());
//        assertEquals(LocalDate.now(), project.getStartDate());
//        assertEquals(LocalDate.now(), project.getEndDate());
//    }


}
