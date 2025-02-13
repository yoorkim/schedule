package com.example.scheduleproject.dto;

import com.example.scheduleproject.entity.Author;
import com.example.scheduleproject.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long id;
    private String todo;
    private String name;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
