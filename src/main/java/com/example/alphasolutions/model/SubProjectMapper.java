package com.example.alphasolutions.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubProjectMapper {

    public List<SubProject> subProjectWithTasks(List<Map<String, Object>> rows) {

        Map<Integer, SubProject> subProjects = new HashMap<>();


        for (Map rs : rows) {
            SubProject subProject = new SubProject();

            subProject.setSubProjectID((Integer) rs.get("SUBPROJECTID"));
            if (subProjects.containsKey(subProject.getSubProjectID())) {
                subProject = subProjects.get(subProject.getSubProjectID());
            } else {
                subProject.setName((String) rs.get("NAME"));
                subProject.setDescription((String) rs.get("DESCRIPTION"));
                subProject.setStartDate(((LocalDateTime) rs.get("STARTDATE")).toLocalDate());
                subProject.setEndDate(((LocalDateTime) rs.get("ENDDATE")).toLocalDate());
                subProject.setTimeEst((Integer) rs.get("TIMEEST"));
                subProject.setTasks(new ArrayList<>());
            }


            Task task = new Task();
            task.setTaskID((int) rs.get("TID"));
            task.setName((String) rs.get("TName"));
            task.setDescription((String) rs.get("TDESCRIPTION"));
            task.setStartDate(((LocalDateTime) rs.get("STARTDATE")).toLocalDate());
            task.setEndDate(((LocalDateTime) rs.get("ENDDATE")).toLocalDate());
            task.setTimeEst((Integer) rs.get("TIMEEST"));


            subProject.addTask(task);

            subProjects.put(subProject.getSubProjectID(), subProject);

        }
        return subProjects.values().stream().toList();
    }
}


