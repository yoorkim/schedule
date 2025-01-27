package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto);
    List<ScheduleResponseDto> findSchedulesByConditions(LocalDate updatedAt, String name);
    List<ScheduleResponseDto> findAllSchedules();
    ScheduleResponseDto findScheduleById(Long id);
    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto);
    void deleteSchedule(Long id, String pwd);
}
