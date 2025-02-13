package com.example.scheduleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private String todo;
    private String pwd;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(String todo, String pwd, Long authorId) {
        this.todo = todo;
        this.pwd = pwd;
        this.authorId = authorId;
    }
}
