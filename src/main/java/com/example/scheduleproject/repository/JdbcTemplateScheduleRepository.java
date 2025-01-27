package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.ScheduleRequestDto;
import com.example.scheduleproject.dto.ScheduleResponseDto;
import com.example.scheduleproject.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
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
        jdbcInsert.usingColumns("todo", "name", "pwd");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("todo", schedule.getTodo());
        parameters.put("name", schedule.getName());
        parameters.put("pwd", schedule.getPwd());

        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
    }

    public List<ScheduleResponseDto> findSchedulesByConditions(LocalDate updatedAt, String name) {
        StringBuilder sql = new StringBuilder("SELECT id, todo, name, created_at, updated_at FROM schedule WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // updatedAt이 주어졌다면 해당 날짜를 처리
        if (updatedAt != null) {
            sql.append(" AND DATE(updated_at) = ?");
            params.add(Date.valueOf(updatedAt));  // LocalDate -> Date로 변환
        }

        // name이 주어졌다면 해당 조건을 추가
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name = ?");
            params.add(name);
        }

        sql.append(" ORDER BY updated_at DESC");  // 내림차순 정렬

        return jdbcTemplate.query(sql.toString(), params.toArray(), scheduleMapper());
    }


    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return jdbcTemplate.query("select id, todo, name, created_at, updated_at from schedule", scheduleMapper());
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("select id, todo, name, created_at, updated_at from schedule where id = ?", scheduleMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    @Override
    public int updateSchedule(Long id, String todo, String name, String pwd) {
        return jdbcTemplate.update("update schedule set todo = ?, name = ? where pwd = ? and id = ?", todo, name, pwd, id);
    }

    @Override
    public int deleteSchedule(Long id, String pwd) {
        return jdbcTemplate.update("delete from schedule where pwd = ? and id = ?", pwd, id);
    }

    private RowMapper<ScheduleResponseDto> scheduleMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }

    private RowMapper<Schedule> scheduleMapperV2() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }
}
