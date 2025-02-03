package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.CreateScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.dto.UpdateScheduleRequestDto;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(CreateScheduleRequestDto requestDto);
    List<ScheduleResponseDto> findSchedulesByConditions(LocalDate updatedAt, Long id);
    List<ScheduleResponseDto> findAllSchedules();
    Map<String, Object> findAllSchedulesPaged(Pageable pageable);
    ScheduleResponseDto findScheduleById(Long id);
    ScheduleResponseDto updateSchedule(Long id, UpdateScheduleRequestDto requestDto);
    void deleteSchedule(Long id, String pwd);
}
