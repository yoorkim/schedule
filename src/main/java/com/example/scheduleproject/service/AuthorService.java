package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {

    Long getOrCreateAuthor(String name, String email);
    List<AuthorResponseDto> findAllAuthors();
    AuthorResponseDto findAuthorById(Long id);
    AuthorResponseDto updateAuthor(Long id,  AuthorRequestDto requestDto);
    void deleteAuthor(Long id);
}
