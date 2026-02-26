package com.example.SpringBoot.tasks;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {  //спец класс для маппинга из TaskEntity в task и наоборот


    public Task toDomain(TaskEntity task){
        return new Task(
                task.getId(),
                task.getCreatorId(),
                task.getAssignedUserId(),
                task.getStatus(),
                task.getCreateDate(),
                task.getDeadlineDate(),
                task.getPriority()
        );
    }

    public TaskEntity toEntity(Task task){
        return new TaskEntity(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.status(),
                task.createDate(),
                task.deadlineDate(),
                task.priority()
        );
    }
}
