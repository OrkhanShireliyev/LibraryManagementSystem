package com.company.librarymanagementsystem.service.inter;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.request.AuthorRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorServiceInter {

    ResponseEntity<AuthorRequest> save(AuthorRequest authorRequest);

    ResponseEntity<Author> update(Long id,String name, String surname,List<Long> bookId);

    ResponseEntity<List<AuthorDTO>> getAllAuthors();

    ResponseEntity<AuthorDTO> getAuthorById(Long id);

    void delete(Long id);
}
