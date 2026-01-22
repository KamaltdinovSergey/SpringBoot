package com.example.SpringBoot.tasks;

public record TaskSearchFilter(
        Long creatorId,

        Long assignedUserId,

        Integer pageSize,

        Integer pageNumber
) {
}
