package com.example.alphasolutions.model;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMapper {

    public List<Project> ProjectWithSubProjects(List<Map<String, Object>> rows) {

        Map<Integer, Project> projects = new HashMap<>();


        for (Map rs : rows) {
            Project project = new Project();

            project.setProjectID((Integer) rs.get("PROJECTID"));
            if (projects.containsKey(project.getProjectID())) {
                project = projects.get(project.getProjectID());
            } else {
                project.setName((String) rs.get("NAME"));
                project.setDescription((String) rs.get("DESCRIPTION"));
                project.setStartDate(((LocalDateTime) rs.get("STARTDATE")).toLocalDate());
                project.setEndDate(((LocalDateTime) rs.get("ENDDATE")).toLocalDate());
                project.setTimeEst((Integer) rs.get("TIMEEST"));
                project.setSubProjects(new ArrayList<>());
            }


            SubProject subProject = new SubProject();
            subProject.setSubProjectID((int)rs.get("SPID"));
            subProject.setName((String) rs.get("SPName"));
            subProject.setDescription((String) rs.get("SPDESCRIPTION"));
            subProject.setStartDate(((LocalDateTime) rs.get("STARTDATE")).toLocalDate());
            subProject.setEndDate(((LocalDateTime) rs.get("ENDDATE")).toLocalDate());
            subProject.setTimeEst((Integer) rs.get("TiMEEST"));



            project.addSubproject(subProject);

            projects.put(project.getProjectID(), project);

        }
        return projects.values().stream().toList();
    }
}
