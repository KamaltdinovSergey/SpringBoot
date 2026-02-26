package com.example.SpringBoot.tasks;

public record TaskSearchFilter( //фильтр для пагинации
        Long creatorId,

        Long assignedUserId,

        Integer pageSize,

        Integer pageNumber
) {
}
