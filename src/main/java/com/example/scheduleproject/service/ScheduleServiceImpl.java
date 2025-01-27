package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;
import com.example.scheduleproject.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        if (requestDto.getName() == null || requestDto.getPwd() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and pwd are required values.");
        }
        Schedule schedule = new Schedule(requestDto.getTodo(), requestDto.getName(), requestDto.getPwd());
        Number key = scheduleRepository.saveSchedule(schedule);

        return new ScheduleResponseDto(scheduleRepository.findScheduleByIdOrElseThrow(key.longValue()));
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByConditions(LocalDate updatedAt, String name) {
        return scheduleRepository.findSchedulesByConditions(updatedAt, name);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllSchedules();
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        if (requestDto.getTodo() == null || requestDto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo and name are required values.");
        }
        if (requestDto.getPwd() == null || requestDto.getPwd().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty.");
        }
        // schedule 수정
        int updatedRow = scheduleRepository.updateSchedule(id, requestDto.getTodo(), requestDto.getName(), requestDto.getPwd());
        // 수정된 row가 0개 라면
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Check your password and id.");
        }

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long id, String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty.");
        }
        int deletedRow = scheduleRepository.deleteSchedule(id, pwd);
        // 삭제된 row가 0개 라면
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Check your password and id");
        }
    }
}
