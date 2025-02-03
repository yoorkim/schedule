package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import org.springframework.data.domain.Pageable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Number saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        // 사용할 컬럼을 명시적으로 설정
        jdbcInsert.usingColumns("todo", "pwd", "author_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("todo", schedule.getTodo());
        parameters.put("pwd", schedule.getPwd());
        parameters.put("author_id", schedule.getAuthorId());

        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByConditions(LocalDate updatedAt, Long id) {
        StringBuilder sql = new StringBuilder("SELECT s.id, s.todo, a.name, a.id AS author_id, s.created_at, s.updated_at FROM schedule s JOIN author a ON s.author_id = a.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // updatedAt이 주어졌다면 해당 날짜를 처리
        if (updatedAt != null) {
            sql.append(" AND DATE(s.updated_at) = ?");
            params.add(Date.valueOf(updatedAt));  // LocalDate -> Date로 변환
        }

        // id가 주어졌다면 해당 조건을 추가
        if (id != null) {
            sql.append(" AND a.id = ?");  // 작성자의 고유 식별자를 통해 조회
            params.add(id);
        }

        sql.append(" ORDER BY s.updated_at DESC");  // 내림차순 정렬

        return jdbcTemplate.query(sql.toString(), params.toArray(), scheduleMapper());
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return jdbcTemplate.query("SELECT s.id, s.todo, a.name, a.id AS author_id, s.created_at, s.updated_at FROM schedule s JOIN author a ON s.author_id = a.id", scheduleMapper());
    }

    @Override
    public Page<ScheduleResponseDto> findAllSchedulesPaged(Pageable pageable) {
        String sql = """
            SELECT s.id, s.todo, a.name, a.id AS author_id, s.created_at, s.updated_at
            FROM schedule s
            JOIN author a ON s.author_id = a.id
            ORDER BY s.created_at DESC
            LIMIT ? OFFSET ?
        """;
        List<ScheduleResponseDto> schedules = jdbcTemplate.query(sql, scheduleMapper(), pageable.getPageSize(), pageable.getOffset());
        Long totalElements = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM schedule", Long.class);

        return new PageImpl<>(schedules, pageable, totalElements);
    }

    @Override
    public ScheduleResponseDto findScheduleByIdOrElseThrow(Long id) {
        List<ScheduleResponseDto> result = jdbcTemplate.query("SELECT s.id, s.todo, a.name, a.id AS author_id, s.created_at, s.updated_at FROM schedule s JOIN author a ON s.author_id = a.id WHERE s.id = ?", scheduleMapper(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    @Override
    public int updateSchedule(Long id, String todo, String pwd) {
        return jdbcTemplate.update("UPDATE schedule SET todo = ? WHERE pwd = ? AND id = ?", todo, pwd, id);
    }

    @Override
    public int deleteSchedule(Long id, String pwd) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE pwd = ? AND id = ?", pwd, id);
    }

    private RowMapper<ScheduleResponseDto> scheduleMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("name"),
                        rs.getLong("author_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }
}
