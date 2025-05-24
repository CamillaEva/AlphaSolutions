package com.example.alphasolutions.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMapper {

    public List<Project> projectWithSubProjects(List<Map<String, Object>> rows) {

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
                project.setSubProjects(new ArrayList<>());
            }

            if (rs.get("SPID") != null) {
                Subproject subProject = new Subproject();

                subProject.setSubprojectID((int) rs.get("SPID"));
                subProject.setName((String) rs.get("SPName"));
                subProject.setDescription((String) rs.get("SPDESCRIPTION"));
                if (rs.get("SPSTARTDATE") != null) {
                    subProject.setStartDate(((LocalDateTime) rs.get("SPSTARTDATE")).toLocalDate());
                }
                if (rs.get("SPENDDATE") != null) {
                    subProject.setEndDate(((LocalDateTime) rs.get("SPENDDATE")).toLocalDate());
                }
                project.createSubproject(subProject);
            }
            projects.put(project.getProjectID(), project);

        }
        return projects.values().stream().toList();
    }
}
