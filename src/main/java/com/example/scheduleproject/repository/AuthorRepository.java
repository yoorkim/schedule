package com.example.scheduleproject.repository;

import com.example.scheduleproject.entity.Author;

import java.util.List;

public interface AuthorRepository {
    Long findAuthorIdByEmail(String email);
    Author saveAuthor(Author author);
    List<Author> findAllAuthors();
    Author findAuthorByIdOrElseThrow(Long id);
    int updateAuthor(Long id, String name, String email);
    int deleteAuthor(Long id);
}
