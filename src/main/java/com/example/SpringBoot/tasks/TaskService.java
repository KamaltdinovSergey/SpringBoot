package com.example.SpringBoot.tasks;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private static Logger log = LoggerFactory.getLogger(TaskService.class);

    public final Map<Long, Task> tasksMap;

    //обычный лонг, который корректно работает в многопотоной среде
    private final AtomicLong idCounter;

    private final TaskRepository taskRepository;

    private  final TaskMapper mapper;

    public TaskService(TaskRepository taskRepository,
                       TaskMapper mapper
    ){
        this.taskRepository = taskRepository;
        this.mapper = mapper;
        tasksMap = new HashMap<>();
        idCounter =new AtomicLong();
    }

    //получение задачи по id
    public Task getTaskById(
            Long id
    ) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Not found task by id = " + id
                ));

        return mapper.toDomain(taskEntity);
    }

    //получение всех задач
    public List<Task> getAllTask(){
        List<TaskEntity> allEntities = taskRepository.findAll();

        List<Task> taskList = allEntities.stream()
                .map(mapper::toDomain)
                .toList();

        return taskList;
    }

    //создание задачи
    public Task createTask(Task taskToCreate) {
//        if(taskToCreate.id() != null){
//            throw new IllegalArgumentException("Id should be empty");
//        } // заменено валидацией
        if(taskToCreate.status() != null){
            throw new IllegalArgumentException("Status should be empty");
        }
        if(taskToCreate.priority() != null){
            throw new IllegalArgumentException("Priority should be empty");
        }
        if(!taskToCreate.deadlineDate().isAfter(taskToCreate.createDate())){
            throw new IllegalArgumentException("Create date must be 1 day earlier than deadline date");
        }

//        var entityToSave = new TaskEntity(
//                null,
//                taskToCreate.creatorId(),
//                taskToCreate.assignedUserId(),
//                Status.CREATED,
//                taskToCreate.createDate(),
//                taskToCreate.deadlineDate(),
//                Priority.Low
//        ); // Заменено на 2 строки кода ниже

        var entityToSave = mapper.toEntity(taskToCreate);
        entityToSave.setStatus(Status.CREATED);

        var savedEntity = taskRepository.save(entityToSave);
        return mapper.toDomain(savedEntity);
    }

    //обновление задачи
    public Task updateTask(
            Long id, Task taskToUpdate
    ) {
        var taskEntity = taskRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Not found task by id = " + id));

        if(!taskToUpdate.deadlineDate().isAfter(taskToUpdate.createDate())){
            throw new IllegalArgumentException("Create date must be 1 day earlier than deadline date");
        }

        TaskEntity updateToTask = null;
        TaskEntity taskToSave = null;
        if (taskEntity.getStatus() == Status.DONE){
            throw new IllegalStateException("Cannot modify task " + taskEntity.getStatus());
        }else if(taskEntity.getStatus() == Status.CREATED){

            updateToTask = mapper.toEntity(taskToUpdate);
            updateToTask.setId(taskEntity.getId());
            updateToTask.setStatus(Status.CREATED);

            taskToSave = taskRepository.save(updateToTask);
        } else if(taskEntity.getStatus() == Status.IN_PROGRESS){
//            updateToTask = new TaskEntity(
//                    taskEntity.getId(),
//                    taskToUpdate.creatorId(),
//                    taskToUpdate.assignedUserId(),
//                    Status.IN_PROGRESS,
//                    taskToUpdate.createDate(),
//                    taskToUpdate.deadlineDate(),
//                    Priority.Low
//            ); заменено кодом 3 строки ниже

            updateToTask = mapper.toEntity(taskToUpdate);
            updateToTask.setId(taskEntity.getId());
            updateToTask.setStatus(Status.IN_PROGRESS);

            taskToSave = taskRepository.save(updateToTask);
        }
        return mapper.toDomain(taskToSave);
    }

    //удаление задачи
    @Transactional
    public void doneTask(Long id) {

        var task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));
        if(task.getStatus().equals(Status.IN_PROGRESS)){
            throw new IllegalStateException("Cannot done in_progress task. Contact with your supervisor");
        }
        if(task.getStatus().equals(Status.DONE)){
            throw new IllegalStateException("Cannot done the task. Task was already done");
        }
        taskRepository.setStatus(id, Status.DONE);
        log.info("Successfully cancelled task: id={}",id);
    }

    //перевод в статус прогресс
    public Task inProgressTask(Long id) {
        var taskEntity = taskRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Not found task by id = " + id));

        if (taskEntity.getStatus() == Status.IN_PROGRESS) {
            throw new IllegalStateException("Cannot progress task " + taskEntity.getStatus());
        }

        taskEntity.setStatus(Status.IN_PROGRESS);
        taskRepository.save(taskEntity);
        return mapper.toDomain(taskEntity);
    }

    //перевод в статус done
    public Task inDoneTask(Long id) {
        var taskEntity = taskRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Not found task by id = " + id));

        if (taskEntity.getStatus() == Status.DONE) {
            throw new IllegalStateException("Cannot done task " + taskEntity.getStatus());
        }

        taskEntity.setStatus(Status.DONE);
        taskRepository.save(taskEntity);
        return mapper.toDomain(taskEntity);
    }

}
