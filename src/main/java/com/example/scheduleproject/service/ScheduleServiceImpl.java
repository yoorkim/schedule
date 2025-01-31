package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.CreateScheduleRequestDto;
import com.example.scheduleproject.dto.UpdateScheduleRequestDto;
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
    private final AuthorService authorService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, AuthorService authorService) {
        this.scheduleRepository = scheduleRepository;
        this.authorService = authorService;
    }

    @Override
    @Transactional
    public ScheduleResponseDto saveSchedule(CreateScheduleRequestDto requestDto) {
        if (requestDto.getTodo() == null || requestDto.getPwd() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo and pwd are required values.");
        }
        if (requestDto.getAuthor().getName() == null || requestDto.getAuthor().getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and email are required values.");
        }

        // 기존 작성자 id or 새로운 작성자 id 가져옴
        Long authorId = authorService.getOrCreateAuthor(requestDto.getAuthor().getName(), requestDto.getAuthor().getEmail());
        Schedule schedule = new Schedule(requestDto.getTodo(), requestDto.getPwd(), authorId);
        Number scheduleKey = scheduleRepository.saveSchedule(schedule);
        return scheduleRepository.findScheduleByIdOrElseThrow(scheduleKey.longValue());
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByConditions(LocalDate updatedAt, Long id) {
        return scheduleRepository.findSchedulesByConditions(updatedAt, id);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllSchedules();
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        return scheduleRepository.findScheduleByIdOrElseThrow(id);
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, UpdateScheduleRequestDto requestDto) {
        if (requestDto.getTodo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo is required values.");
        }
        if (requestDto.getPwd() == null || requestDto.getPwd().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty.");
        }
        // schedule 수정
        int updatedRow = scheduleRepository.updateSchedule(id, requestDto.getTodo(), requestDto.getPwd());
        // 수정된 row가 0개 라면
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Check your password and id.");
        }

        return scheduleRepository.findScheduleByIdOrElseThrow(id);
    }

    @Override
    public void deleteSchedule(Long id, String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be null or empty.");
        }
        int deletedRow = scheduleRepository.deleteSchedule(id, pwd);
        // 삭제된 row가 0개 라면
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Check your password and id.");
        }
    }
}
