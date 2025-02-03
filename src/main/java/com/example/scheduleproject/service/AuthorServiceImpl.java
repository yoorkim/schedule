package com.example.scheduleproject.service;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.entity.Author;
import com.example.scheduleproject.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Long getOrCreateAuthor(String name, String email) {
        Long authorId = authorRepository.findAuthorIdByEmail(email);

        if (authorId == null) {
            Author author = new Author(name, email);  // 새로운 작성자 생성
            authorId = authorRepository.saveAuthor(author).getId();
        }

        return authorId;
    }

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        List<Author> authors = authorRepository.findAllAuthors();
        List<AuthorResponseDto> dtoList = new ArrayList<>();

        for (Author author : authors) {
            dtoList.add(new AuthorResponseDto(author));
        }

        return dtoList;
    }

    @Override
    public AuthorResponseDto findAuthorById(Long id) {
        return new AuthorResponseDto(authorRepository.findAuthorByIdOrElseThrow(id));
    }

    @Override
    @Transactional
    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto requestDto) {
        if (requestDto.getName() == null || requestDto.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and Email are required values.");
        }

        int updatedRow = authorRepository.updateAuthor(id, requestDto.getName(), requestDto.getEmail());
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid author id.");
        }

        return new AuthorResponseDto(authorRepository.findAuthorByIdOrElseThrow(id));
    }

    @Override
    public void deleteAuthor(Long id) {
        int deletedRow = authorRepository.deleteAuthor(id);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid author id.");
        }
    }
}
