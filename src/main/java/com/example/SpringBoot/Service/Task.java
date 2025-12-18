package com.example.SpringBoot.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Task (
        Long id,

        Long creatorId,

        Long assignedUserId,

        Status status,

        LocalDate createDate,

        LocalDate deadlineDate,

        Priority priority
){
}
