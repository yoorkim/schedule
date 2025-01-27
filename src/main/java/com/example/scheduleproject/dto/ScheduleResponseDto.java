package com.example.scheduleproject.dto;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.todo = schedule.getTodo();
        this.name = schedule.getName();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
