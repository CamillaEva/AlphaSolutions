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
                //prøver lige her:
                subProject.setProjectID((Integer) rs.get("PROJECTID"));
                subProject.setTasks(new ArrayList<>());
            }


            if (rs.get("TID") != null) {
                Task task = new Task();

                task.setTaskID((int) rs.get("TID"));
                task.setSubProjectID((Integer) rs.get("TSUBPROJECTID"));
                task.setName((String) rs.get("TName"));
                task.setDescription((String) rs.get("TDESCRIPTION"));
                if (rs.get("TSTARTDATE") != null) {
                    task.setStartDate(((LocalDateTime) rs.get("TSTARTDATE")).toLocalDate());
                }
                if (rs.get("TENDDATE") != null) {
                    task.setEndDate(((LocalDateTime) rs.get("TENDDATE")).toLocalDate());
                }
                if (rs.get("TTIMEEST") != null) {
                    task.setTimeEst((Integer) rs.get("TTIMEEST"));
                }
                subProject.createTask(task);
            }

            subProjects.put(subProject.getSubProjectID(), subProject);


        }


        return subProjects.values().stream().toList();
    }
}




