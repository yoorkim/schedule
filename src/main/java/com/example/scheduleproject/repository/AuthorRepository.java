package com.example.scheduleproject.repository;

import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;

import java.util.List;

public interface AuthorRepository {
    Long findAuthorIdByEmail(String email);
    Number saveAuthor(Author author);
    List<AuthorResponseDto> findAllAuthors();
    AuthorResponseDto findAuthorByIdOrElseThrow(Long id);
    int updateAuthor(Long id, String name, String email);
    int deleteAuthor(Long id);
}
