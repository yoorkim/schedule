package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {

    Number saveSchedule(Schedule schedule);
    List<ScheduleResponseDto> findSchedulesByConditions(LocalDate updatedAt, Long id);
    List<ScheduleResponseDto> findAllSchedules();
    ScheduleResponseDto findScheduleByIdOrElseThrow(Long id);
    int updateSchedule(Long id, String todo, String pwd);
    int deleteSchedule(Long id, String pwd);
}
