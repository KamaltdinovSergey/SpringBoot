package com.example.SpringBoot.Service;

import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    public final Map<Long, Task> tasksMap;

    //обычный лонг, который корректно работает в многопотоной среде
    private final AtomicLong idCounter;

    public TaskService(){
        tasksMap = new HashMap<>();
        idCounter =new AtomicLong();
    }

    public Task getTaskById(
            Long id) throws NoSuchFieldException {
        if(!tasksMap.containsKey(id)){
            throw new NoSuchFieldException("Not found" + id);
        }
        return tasksMap.get(id);
    }

    public List<Task> getAllTask(){
        return tasksMap.values().stream().toList();
    }

    public Task createTask(Task taskToCreate) {
        if(taskToCreate.id() != null){
            throw new IllegalArgumentException("Id should be empty");
        }
        if(taskToCreate.status() != null){
            throw new IllegalArgumentException("Status should be empty");
        }
        if(taskToCreate.priority() != null){
            throw new IllegalArgumentException("Priority should be empty");
        }
        var newTask = new Task(
                idCounter.incrementAndGet(),
                taskToCreate.creatorId(),
                taskToCreate.assignedUserId(),
                Status.CREATED,
                taskToCreate.createDate(),
                taskToCreate.deadlineDate(),
                Priority.Low
        );
        tasksMap.put(newTask.id(), newTask);
        return newTask;
    }

    public Object updateTask(
            Long id, Task taskToUpdate
    ) {
        if(!tasksMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id =" + id);
        }
        var task = tasksMap.get(id);
        Task updateTask = null;
        if (task.status() == Status.DONE){
            throw new IllegalStateException("Cannot modify task " + task.status());
        }else if(task.status() == Status.CREATED){
            updateTask = new Task(
                    task.id(),
                    taskToUpdate.creatorId(),
                    taskToUpdate.assignedUserId(),
                    Status.CREATED,
                    taskToUpdate.createDate(),
                    taskToUpdate.deadlineDate(),
                    Priority.Low
            );
            tasksMap.put(task.id(), updateTask);
        } else if(task.status() == Status.IN_PROGRESS){
            updateTask = new Task(
                    task.id(),
                    taskToUpdate.creatorId(),
                    taskToUpdate.assignedUserId(),
                    Status.IN_PROGRESS,
                    taskToUpdate.createDate(),
                    taskToUpdate.deadlineDate(),
                    Priority.Low
            );
            tasksMap.put(task.id(), updateTask);
        }
        return updateTask;
    }

    public void deleteTask(Long id) {
        if(!tasksMap.containsKey(id)){
            throw new NoSuchElementException("Not found task by id =" + id);
        }
        tasksMap.remove(id);
    }

    public Object inProgressTask(Long id) {
        if (!tasksMap.containsKey(id)) {
            throw new NoSuchElementException("Not found task by id =" + id);
        }
        var task = tasksMap.get(id);
        if (task.status() == Status.IN_PROGRESS) {
            throw new IllegalStateException("Cannot progress task " + task.status());
        }
        var progressTask = new Task(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                Status.IN_PROGRESS,
                task.createDate(),
                task.deadlineDate(),
                Priority.Low
        );
        tasksMap.put(task.id(), progressTask);
        return progressTask;
    }

    public Object inDoneTask(Long id) {
        if (!tasksMap.containsKey(id)) {
            throw new NoSuchElementException("Not found task by id =" + id);
        }
        var task = tasksMap.get(id);
        if (task.status() == Status.DONE) {
            throw new IllegalStateException("Cannot done task " + task.status());
        }
        var doneTask = new Task(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                Status.DONE,
                task.createDate(),
                task.deadlineDate(),
                Priority.Low
        );
        tasksMap.put(task.id(), doneTask);
        return doneTask;
    }

}
