package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.mapper.BookMapper;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.repository.AuthorRepository;
import com.company.librarymanagementsystem.repository.BookRepository;
import com.company.librarymanagementsystem.repository.CategoryRepository;
import com.company.librarymanagementsystem.repository.StudentRepository;
import com.company.librarymanagementsystem.request.BookRequest;
import com.company.librarymanagementsystem.service.inter.BookServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookServiceInter {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<BookRequest> save(BookRequest bookRequest) {
        try {
            Book book = bookMapper.bookRequestToBook(bookRequest);
            bookRepository.save(book);
            log.info("Successfully created{}", bookRequest);
            return new ResponseEntity<>(bookRequest, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when creating book!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Book> update(Long id,
                                       String name,
                                       String isbn,
                                       String publishedYear,
                                       String image,
                                       Long stockCount,
                                       List<Long> authorId,
                                       Long categoryId,
                                       List<Long> studentId) {
        Book book = bookRepository.findById(id).get();
        if (book == null) {
            throw new NoSuchElementException("Not found book by id=" + id);
        }

        List<Author> authors = new ArrayList<>();
        for (Long authorById : authorId) {
            Author findAuthorById = authorRepository.findById(authorById).get();
            if (findAuthorById == null) {
                throw new NoSuchElementException("Not found author by id=" + authorById);
            }
            authors.add(findAuthorById);
        }

        Category category = categoryRepository.findById(categoryId).get();
        if (category == null) {
            throw new NoSuchElementException("Not found category by id=" + categoryId);
        }

        List<Student> students = new ArrayList<>();
        for (Long studentById : studentId) {
            Student findStudentById = studentRepository.findById(studentById).get();
            if (findStudentById == null) {
                throw new NoSuchElementException("Not found student by id=" + studentById);
            }
            students.add(findStudentById);
        }

        try {
            book.setName(name);
            book.setIsbn(isbn);
            book.setPublishedYear(publishedYear);
            book.setImage(image);
            book.setStockCount(stockCount);
            book.setAuthors(authors);
            book.setCategory(category);
            book.setStudents(students);
            bookRepository.save(book);
            log.info("Successfully updated{}", book);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when updating book!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book>books=bookRepository.findAll();
        if (books==null){
            throw new NullPointerException("Not found books!");
        }

        List<BookDTO>bookDTOS=new ArrayList<>();
        BookDTO bookDTO;
        try{
            for(Book book:books){
                bookDTO=bookMapper.bookToBookDTO(book);
                bookDTOS.add(bookDTO);
            }
            log.info("Successfully retrieved{}",bookDTOS);
            return new ResponseEntity<>(bookDTOS,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when retrieving books!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BookDTO> getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Not found book by id=" + id));

        try{
            BookDTO bookDTO=bookMapper.bookToBookDTO(book);
            log.info("Successfully retrieved{}",bookDTO);
            return new ResponseEntity<>(bookDTO,HttpStatus.OK);
        }catch (Exception e){
        log.error("Error occurred when retrieving book by id="+id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Not found book by id=" + id));
        try {
            bookRepository.deleteById(id);
            log.info("Successfully deleted{}",book);
            return new ResponseEntity<>("Successfully deleted{"+book+"}",HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occured while retrieving book by id="+id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
