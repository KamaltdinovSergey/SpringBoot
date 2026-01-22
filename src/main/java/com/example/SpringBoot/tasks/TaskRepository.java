package com.example.SpringBoot.tasks;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Modifying
    @Query("""
            update TaskEntity r
            set r.status = :status
            where r.id = :id
            """)
    void setStatus(
            @Param("id") Long id,
            @Param("status") Status status);

//    @Query("""
//            SELECT r.id from TaskEntity r
//            WHERE (:taskId IS NULL OR r.taskId = :taskId)
//            AND (:userId IS NULL OR r.userId = :userId)
//            """)
//    List<TaskEntity> searchAllByFilter(
//            @Param("taskId") Long taskId,
//            @Param("userId") Long userId,
//            Pageable pageable
//            //pageable - это специальный объект который говорит спрингу какую страницу
//            // нужно выдать и сколько элементов должно быть на странице
//    );
}
