package com.example.scheduleproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import jakarta.validation.constraints.Email;

@Getter
public class AuthorRequestDto {

    @NotNull(message = "name은 필수 입력 값입니다.")
    private String name;
    @Email(message = "이메일 형식에 맞게 작성해주세요.")
    @NotBlank(message = "email은 필수 입력 값입니다.")
    private String email;

}
