package com.example.alphasolutions.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectMapper {

    public List<Project> projectWithSubprojects(List<Map<String, Object>> rows) {

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
                project.setSubprojects(new ArrayList<>());
            }

            if (rs.get("SPID") != null) {
                Subproject subproject = new Subproject();

                subproject.setSubprojectID((int) rs.get("SPID"));
                subproject.setName((String) rs.get("SPName"));
                subproject.setDescription((String) rs.get("SPDESCRIPTION"));
                if (rs.get("SPSTARTDATE") != null) {
                    subproject.setStartDate(((LocalDateTime) rs.get("SPSTARTDATE")).toLocalDate());
                }
                if (rs.get("SPENDDATE") != null) {
                    subproject.setEndDate(((LocalDateTime) rs.get("SPENDDATE")).toLocalDate());
                }
                project.createSubproject(subproject);
            }
            projects.put(project.getProjectID(), project);

        }
        return projects.values().stream().toList();
    }
}
