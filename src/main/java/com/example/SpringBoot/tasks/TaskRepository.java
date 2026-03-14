package com.example.SpringBoot.tasks;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//repository приходит из стартера Data JPA с спец методами для работы с БД при наследовании от JpaRepository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> { //используется для работы с БД

//    примеры написания кастомных методов для запросов в бд через интерфейс repository с помощью аннотации query
//    @Query(value = "select * from tasks r where r.status = :status", nativeQuery = true)
//    List<TaskEntity> findAllByStatusIs(Status status);





    //метод setStatus написан в репозитории кастомным запросом через query
    @Modifying
    @Query("""
            UPDATE TaskEntity r
            SET r.status = :status
            WHERE r.id = :id
            """)
    void setStatus(
            @Param("id") Long id,
            @Param("status") Status status);


    @Query("""
            SELECT r.id from TaskEntity r
            WHERE (:creatorId IS NULL OR r.creatorId = :creatorId)
            AND (:assignedUserId IS NULL OR r.assignedUserId = :assignedUserId)
            """)
    List<TaskEntity> searchAllByFilter( //специальный метод для фильтра
            @Param("taskId") Long creatorId,
            @Param("userId") Long assignedUserId,
            Pageable pageable
            //pageable - это специальный объект который говорит спрингу какую страницу
            // нужно выдать и сколько элементов должно быть на странице
    );

    //пример методов которые реализутся автоматически
    List<TaskEntity> findAllByStatus(Status status);

    int countAllByStatus(Status status);

    Optional<TaskEntity> findByFirstStatus(Status status);
}
