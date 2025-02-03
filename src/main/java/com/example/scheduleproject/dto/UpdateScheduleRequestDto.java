package com.example.scheduleproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateScheduleRequestDto {

    @Size(max = 200, message = "200자 이내로 입력해주세요.")
    @NotNull(message = "todo는 필수 입력 값입니다.")
    private String todo;
    @NotNull(message = "pwd는 필수 입력 값입니다.")
    private String pwd;

}
