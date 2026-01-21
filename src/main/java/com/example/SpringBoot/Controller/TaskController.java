package com.example.SpringBoot.Controller;

import com.example.SpringBoot.Service.Task;
import com.example.SpringBoot.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("id") Long id
    ) throws NoSuchFieldException {
        log.info("Called getTaskById");
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks(){
        log.info("getAllTask");
        return ResponseEntity.ok(taskService.getAllTask());
    }

    @PostMapping()
    public ResponseEntity<Task> createTask(
            @RequestBody Task taskToCreate
    ){
        log.info("Called createTask");
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("test-header", "123")
                .body(taskService.createTask(taskToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable("id") Long id,
            @RequestBody Task taskToUpdate
    ){
        log.info("Called updateTask id={}, taskToUpdate={}", id, taskToUpdate);
        var updated = taskService.updateTask(id, taskToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/done")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id
    ){
        log.info("Called deleteTask: id={}", id);
        try{
            taskService.doneTask(id);
            return ResponseEntity.ok()
                    .build();
        }catch (NoSuchElementException e){
            return ResponseEntity.status(404)
                    .build();
        }
    }
    @PostMapping("/{id}/progress")
    public ResponseEntity<?> inProgressTask(
            @PathVariable("id") Long id
    ){
        log.info("Called workTask: id={}", id);
        var task = taskService.inProgressTask(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{id}/done")
    public ResponseEntity<?> InDoneTask(
            @PathVariable("id") Long id
    ){
        log.info("Called inDoneTask: id={}", id);
        var task = taskService.inDoneTask(id);
        return ResponseEntity.ok(task);
    }
}
