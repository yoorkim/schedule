package com.example.scheduleproject.controller;

import com.example.scheduleproject.dto.AuthorRequestDto;
import com.example.scheduleproject.dto.AuthorResponseDto;
import com.example.scheduleproject.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")  // Prefix
public class AuthorController {

    private final AuthorService authorService;

    private AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@RequestBody AuthorRequestDto requestDto) {
        Long id = authorService.getOrCreateAuthor(requestDto.getName(), requestDto.getEmail());
        return new ResponseEntity<>(authorService.findAuthorById(id), HttpStatus.OK);
    }

    @GetMapping
    public List<AuthorResponseDto> findAllAuthors() {
        return authorService.findAllAuthors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> findAuthorById(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.findAuthorById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @PathVariable Long id,
            @RequestBody AuthorRequestDto requestDto
    ) {
        return new ResponseEntity<>(authorService.updateAuthor(id, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
