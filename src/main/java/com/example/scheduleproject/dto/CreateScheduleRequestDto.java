package com.example.scheduleproject.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequestDto {

    private String todo;
    private String pwd;
    private AuthorRequestDto author;
}
