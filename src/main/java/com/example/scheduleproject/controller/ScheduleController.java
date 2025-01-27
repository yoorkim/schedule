package com.example.scheduleproject.controller;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")  // Prefix
public class ScheduleController {

    private final ScheduleService scheduleService;

    private ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<List<ScheduleResponseDto>> findSchedules(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate updatedAt,
            @RequestParam(required = false) String name) {
        return new ResponseEntity<>(scheduleService.findSchedulesByConditions(updatedAt, name), HttpStatus.OK);
    }

    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {

        return scheduleService.findAllSchedules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {

        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestBody String pwd) {
        scheduleService.deleteSchedule(id, pwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
