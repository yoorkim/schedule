package com.example.scheduleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private String todo;
    private String name;
    private String pwd;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(String todo, String name, String pwd) {
        this.todo = todo;
        this.name = name;
        this.pwd = pwd;
    }

    public Schedule(Long id, String todo, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.todo = todo;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
