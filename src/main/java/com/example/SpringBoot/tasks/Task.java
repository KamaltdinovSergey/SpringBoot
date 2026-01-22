package com.example.SpringBoot.tasks;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

public record Task (
        @Null
        Long id,

        @NotNull
        Long creatorId,

        @NotNull
        Long assignedUserId,

        Status status,

        @FutureOrPresent
        @NotNull
        LocalDate createDate,

        @FutureOrPresent
        @NotNull
        LocalDate deadlineDate,

        Priority priority
){
}
