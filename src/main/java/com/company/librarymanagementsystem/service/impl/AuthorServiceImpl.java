package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.exception.NotFoundException;
import com.company.librarymanagementsystem.mapper.AuthorMapper;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.repository.AuthorRepository;
import com.company.librarymanagementsystem.repository.BookRepository;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.service.inter.AuthorServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorServiceInter {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final BookRepository bookRepository;

    @Override
    public ResponseEntity<AuthorRequest> save(AuthorRequest authorRequest,List<Long> bookId) {
        List<Book> books = bookRepository.findAllById(bookId);

        if (books.isEmpty()) {
            throw new NotFoundException("Not found books!");
        }

        try {
            List<Author> authors = new ArrayList<>();
            Author author = new Author();
            authors.add(author);
            author.setName(authorRequest.getName());
            author.setSurname(authorRequest.getSurname());

            for (Long bookIds : bookId) {
                Book book = bookRepository.findById(bookIds)
                        .orElseThrow(() -> new NoSuchElementException("Not found book by id=" + bookIds));
                book.getAuthors().add(author);
            }

            author.setBooks(books);
            authorRepository.save(author);
            bookRepository.saveAll(books);
            log.info("Successfully created {}", author);
            return new ResponseEntity<>(authorRequest, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when creating authors!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Author> update(Long id,String name, String surname,List<Long> bookIds) {
        Author author=authorRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Not found author by id="+id));

        List<Book> books = bookRepository.findAllById(bookIds);
        if (books.isEmpty()) {
            throw new RuntimeException("Not found books!");
        }

        try{
            for (Book book : author.getBooks()) {
                book.getAuthors().remove(author);
            }

            author.getBooks().clear();

            for (Book book : books) {
                book.getAuthors().add(author);
            }

            author.setName(name);
            author.setSurname(surname);
            author.setBooks(books);
            authorRepository.save(author);
            bookRepository.saveAll(books);
            log.info("Successfully updated {}",author);
            return new ResponseEntity<>(author, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when updating author!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<Author> authors=authorRepository.findAll();
        authors.stream().forEach(System.out::println);
        if (authors==null || authors.isEmpty()){
            throw new NoSuchElementException("Not found authors!");
        }
        List<AuthorDTO>authorDTOS=new ArrayList<>();
        AuthorDTO authorDTO;
        try {
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
        Author author=authorRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Not found author by id="+id));

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
        Author author=authorRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Not found author by id="+id));
        System.out.println(author);
        try{
            authorRepository.deleteById(id);
            log.info("Successfully deleted{}",author);
        }catch (Exception e){
            log.error("Error occurred when deleting author by id="+id);
        }
    }
}
