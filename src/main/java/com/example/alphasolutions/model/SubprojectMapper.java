package com.example.alphasolutions.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubprojectMapper {

    public List<Subproject> subprojectWithTasks(List<Map<String, Object>> rows) {

        Map<Integer, Subproject> subprojects = new HashMap<>();

        for (Map rs : rows) {
            Subproject subproject = new Subproject();

            subproject.setSubprojectID((Integer) rs.get("SUBPROJECTID"));
            if (subprojects.containsKey(subproject.getSubprojectID())) {
                subproject = subprojects.get(subproject.getSubprojectID());
            } else {
                subproject.setName((String) rs.get("NAME"));
                subproject.setDescription((String) rs.get("DESCRIPTION"));
                subproject.setStartDate(((LocalDateTime) rs.get("STARTDATE")).toLocalDate());
                subproject.setEndDate(((LocalDateTime) rs.get("ENDDATE")).toLocalDate());
                subproject.setProjectID((Integer) rs.get("PROJECTID"));
                subproject.setTasks(new ArrayList<>());
            }


            if (rs.get("TID") != null) {
                Task task = new Task();

                task.setTaskID((int) rs.get("TID"));
                task.setSubprojectID((Integer) rs.get("TSUBPROJECTID"));
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
                subproject.createTask(task);
            }
            subprojects.put(subproject.getSubprojectID(), subproject);
        }

        return subprojects.values().stream().toList();
    }
}




