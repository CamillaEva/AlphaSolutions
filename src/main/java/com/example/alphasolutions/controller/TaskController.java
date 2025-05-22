package com.example.alphasolutions.controller;

import com.example.alphasolutions.model.*;
import com.example.alphasolutions.service.EmpService;
import com.example.alphasolutions.service.ProjectService;
import com.example.alphasolutions.service.SubprojectService;
import com.example.alphasolutions.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final SubprojectService subprojectService;
    private final ProjectService projectService;
    private final EmpService empService;

    public TaskController(TaskService taskService, SubprojectService subprojectService,
                          ProjectService projectService, EmpService empService) {
        this.taskService = taskService;
        this.subprojectService = subprojectService;
        this.projectService = projectService;
        this.empService = empService;
    }

    //_______________________________________________CREATE_____________________________________________________________
    @GetMapping("/create-task/{subProjectID}")
    public String createTask(@PathVariable("subProjectID") int subProjectID, HttpSession session, Model model) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Subproject subProject = subprojectService.readSubProjectByID(subProjectID);
            model.addAttribute("subProject", subProject);
            model.addAttribute("task", new Task());
            return "create-task";
        }
        return "error/no-access";

    }

    @PostMapping("/create-task/{subProjectID}/add")
    public String saveTask(@PathVariable("subProjectID") int subProjectID, @ModelAttribute Task task,
                           HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            task.setSubProjectID(subProjectID);
            int newTaskID = taskService.createTask(task);

            //puts new task in subproject_tasks in the database
            subprojectService.assignTaskToSubproject(newTaskID, subProjectID);

            return "redirect:/read-subproject/" + subProjectID;
        }
        return "error/no-access";
    }

    //_______________________________________________READ_______________________________________________________________
    @GetMapping("/read-tasks/{taskID}")
    public String readTaskByID(@PathVariable int taskID, Model model,
                               HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");
        Employee sessionEmp = (Employee) session.getAttribute("emp");

        if (sessionRole == Role.PROJECT_LEADER || sessionRole == Role.EMPLOYEE) {
            Task task = taskService.readTaskByID(taskID);
            Subproject subproject = subprojectService.readSubProjectByID(task.getSubProjectID());
            Project project = projectService.readProjectByID(subproject.getProjectID());


            int totalTimeEstimate = taskService.readTotalTimeEstimateForProject(project.getProjectID());
            //Method to get totalTimeUsed for tasks in a project
            int totalTimeUsed = taskService.readTotalUsedTimeForProject(project.getProjectID());

            List<Integer> assignedEmpIDsTask = taskService.showAssignedEmpTask(taskID);
            List<Employee> assignedEmployeesTask = new ArrayList<>();

            for (int empID : assignedEmpIDsTask) {
                assignedEmployeesTask.add(empService.readEmployeeById(empID));
            }

            List<Integer> assignedEmpIDsProject = projectService.showAssignedEmpProject(project.getProjectID());
            List<Employee> assignedEmployeesProject = new ArrayList<>();

            for (int empID : assignedEmpIDsProject) {
                assignedEmployeesProject.add(empService.readEmployeeById(empID));
            }

            model.addAttribute("subproject", subproject);
            model.addAttribute("sessionEmp", sessionEmp);
            model.addAttribute("assignedEmployeesProject", assignedEmployeesProject);
            model.addAttribute("assignedEmployeesTask", assignedEmployeesTask);
            model.addAttribute("task", task);
            model.addAttribute("totalTimeEstimate", totalTimeEstimate);
            model.addAttribute("totalTimeUsed", totalTimeUsed);
            return "read-task";
        }
        return "error/no-access";
    }

    //_______________________________________________UPDATE_____________________________________________________________
    @GetMapping("/pl/edit-task/{taskID}")
    public String editTask(@PathVariable int taskID, Model model, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Task task = taskService.readTaskByID(taskID);
            model.addAttribute("task", task);
            return "update-task";
        }
        return "error/no-access";
    }

    @PostMapping("/pl/edit-task/{taskID}")
    public String updateTask(@PathVariable int taskID, @ModelAttribute("task") Task task,
                             HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            taskService.updateTask(task);
            return "redirect:/read-tasks/" + taskID;
        }
        return "error/no-access";
    }

    @PostMapping("/emp/edit-task/{taskID}")
    public String updateUsedTime(@PathVariable int taskID, @ModelAttribute("task") Task task) {
        taskService.updateUsedTime(task);
        return "redirect:/read-tasks/" + taskID;
    }


    //_______________________________________________DELETE_____________________________________________________________
    @PostMapping("/delete-task/{taskID}")
    public String deleteSubProject(@PathVariable int taskID, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            Task task = taskService.readTaskByID(taskID);
            taskService.deleteTask(task);
            return "redirect:/read-subproject/" + task.getSubProjectID();
        }
        return "error/no-access";
    }

    //_____________________________________________ASSIGN_______________________________________________________________
    @GetMapping("/read-tasks/{taskID}/assign-emp")
    public String assignEmpToTask(@PathVariable int taskID, Model model, HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            List<Employee> employees = empService.readAllEmployees();
            List<Integer> alreadyAssigned = taskService.showAssignedEmpTask(taskID);

            model.addAttribute("alreadyAssigned", alreadyAssigned);
            model.addAttribute("taskID", taskID);
            model.addAttribute("employees", employees);
            return "assign-emp";
        }
        return "error/no-access";
    }

    @PostMapping("/read-tasks/{taskID}/assign-emp")
    public String assignEmpToTask(@PathVariable int taskID,
                                  @RequestParam("empSelected") List<Integer> selectedEmpIDs,
                                  HttpSession session) {
        Role sessionRole = (Role) session.getAttribute("role");

        if (sessionRole == Role.PROJECT_LEADER) {
            if (selectedEmpIDs != null) {
                for (int empID : selectedEmpIDs) {
                    taskService.assignEmpToTask(taskID, empID);
                }
            }
            return "redirect:/read-tasks/" + taskID;
        }
        return "error/no-access";
    }


}
