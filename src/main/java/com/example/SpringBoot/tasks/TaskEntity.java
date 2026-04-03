package com.example.SpringBoot.tasks;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@Entity
public class TaskEntity { //сущность для хабернейта, которую он сохраняет в базу данных

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //автоматическая генерация id со спец стратегией (IDENTITY)
    private Long id;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "assigned_user_id", nullable = false)
    private Long assignedUserId;

    @Enumerated(EnumType.STRING)  //указываем что данные в этой строке в БД будет строка
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "create_date_id", nullable = false)
    private LocalDate createDate;

    @Column(name = "deadline_date", nullable = false)
    private LocalDate deadlineDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

}
