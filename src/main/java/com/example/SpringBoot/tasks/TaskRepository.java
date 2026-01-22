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

    @Query("""
            SELECT r.id from TaskEntity r
            WHERE (:creatorId IS NULL OR r.creatorId = :creatorId)
            AND (:assignedUserId IS NULL OR r.assignedUserId = :assignedUserId)
            """)
    List<TaskEntity> searchAllByFilter(
            @Param("taskId") Long creatorId,
            @Param("userId") Long assignedUserId,
            Pageable pageable
            //pageable - это специальный объект который говорит спрингу какую страницу
            // нужно выдать и сколько элементов должно быть на странице
    );
}
