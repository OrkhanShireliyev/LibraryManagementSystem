package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.mapper.AuthorMapper;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.repository.AuthorRepository;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.service.inter.AuthorServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorServiceInter {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public ResponseEntity<Author> save(AuthorRequest authorRequest) {
        try {
            Author author = authorMapper.authorRequestToAuthor(authorRequest);
            authorRepository.save(author);
            log.info("Successfully created{}",author);
            return new ResponseEntity<>(author, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when creating authors!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Author> update(Long id,String name, String surname) {
        Author author=authorRepository.findById(id).get();
        if (author==null){
            throw new NoSuchElementException("Not found author by id="+id);
        }
        try{
            author.setName(name);
            author.setSurname(surname);
            authorRepository.save(author);
            log.info("Successfully updated{}",author);
            return new ResponseEntity<>(author, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when updating author!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<Author> authors=authorRepository.findAll();
        if (authors==null){
            throw new NullPointerException("Not found authors!");
        }
        List<AuthorDTO>authorDTOS=new ArrayList<>();
        try {
            AuthorDTO authorDTO;
            for (Author author : authors) {
                authorDTO = authorMapper.authorToAuthorDTO(author);
                authorDTOS.add(authorDTO);
            }
            log.info("Successfully retrieved{}",authorDTOS);
            return new ResponseEntity<>(authorDTOS,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when retrieving authors!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<AuthorDTO> getAuthorById(Long id) {
        Author author=authorRepository.findById(id).get();
        if (author==null){
            throw new NoSuchElementException("Not found author by id="+id);
        }
        try{
            AuthorDTO authorDTO=authorMapper.authorToAuthorDTO(author);
            log.info("Successfully retrieved{}",authorDTO);
            return new ResponseEntity<>(authorDTO,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when retrieving author by id="+id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void delete(Long id) {
        Author author=authorRepository.findById(id).get();
        if (author==null){
            throw new NoSuchElementException("Not found author by id="+id);
        }
        try{
            authorRepository.deleteById(id);
            log.info("Successfully deleted author by id="+id);
        }catch (Exception e){
            log.error("Error occurred when deleting author by id="+id);
        }
    }
}
