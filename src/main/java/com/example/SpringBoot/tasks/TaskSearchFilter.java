package com.example.SpringBoot.tasks;

public record TaskSearchFilter(
        Long taskId,

        Long userId,

        Integer pageSize,

        Integer pageNumber
) {
}
