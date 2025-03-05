package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.mapper.BookMapper;
import com.company.librarymanagementsystem.model.*;
import com.company.librarymanagementsystem.repository.*;
import com.company.librarymanagementsystem.service.S3Service;
import com.company.librarymanagementsystem.service.inter.BookServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookServiceInter {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final StudentRepository studentRepository;

    private final OrderRepository orderRepository;

    private final S3Service s3Service;

    @Override
    public ResponseEntity<BookDTO> save(String isbn,
                                        String name,
                                        String publishedYear,
                                        Long stockCount,
                                        MultipartFile image,
                                        List<Long> authorId,
                                        List<Long> studentId,
                                        Long categoryId,
                                        List<Long> orderId) throws IOException {

        List<Author> authors = authorRepository.findAllById(authorId);
        if (authors.isEmpty()) {
            throw new NoSuchElementException("Not found authors!");
        }

        List<Student> students = studentRepository.findAllById(studentId);
        if (students.isEmpty()) {
            throw new NoSuchElementException("Not found students!");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Not found category by id=" + categoryId));

        List<Order> orders = orderRepository.findAllById(orderId);
        if (orders == null) {
            throw new NoSuchElementException("Not found order!");
        }

        String imageUrl;
        try {
            imageUrl = s3Service.uploadFile(bucketName, image.getOriginalFilename(),
                    image.getInputStream(), image.getSize());
        } catch (IOException e) {
            throw new IOException("Could not load image: " + e);
        }
        try {
            Book book = new Book();
            for (Long orderIds:orderId){
                Order order=orderRepository.findById(orderIds)
                        .orElseThrow(() -> new NoSuchElementException("Not found order by id=" + orderIds));
                order.getBooks().add(book);
                orderRepository.save(order);
            }
            book.setName(name);
            book.setIsbn(isbn);
            book.setPublishedYear(publishedYear);
            book.setStockCount(stockCount);
            book.setImage(imageUrl);
            book.setAuthors(authors);
            book.setStudents(students);
            book.setCategory(category);
            book.setOrders(orders);
            bookRepository.save(book);
            orderRepository.saveAll(orders);
            BookDTO bookDTO = bookMapper.bookToBookDTO(book);
            log.info("Successfully created{}", bookDTO);
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when creating book! ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<BookDTO> update(Long id,
                                       String name,
                                       String isbn,
                                       String publishedYear,
                                       MultipartFile image,
                                       Long stockCount,
                                       List<Long> authorId,
                                       Long categoryId,
                                       List<Long> studentId,
                                       List<Long> orderId) throws IOException {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found author by id=" + id));

        List<Author> authors = new ArrayList<>();
        for (Long authorById : authorId) {
            Author findAuthorById = authorRepository.findById(authorById)
                    .orElseThrow(() -> new NoSuchElementException("Not found author by id=" + authorById));
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

        List<Order> orders = orderRepository.findAllById(orderId);
        if (orders == null) {
            throw new NoSuchElementException("Not found order!");
        }
        for (Long orderIds:orderId){
            Order order=orderRepository.findById(orderIds)
                    .orElseThrow(() -> new NoSuchElementException("Not found order by id=" + orderIds));
            order.getBooks().add(book);
        }


        String imageUrl;
        try {
            imageUrl = s3Service.uploadFile(bucketName, image.getOriginalFilename(),
                    image.getInputStream(), image.getSize());
        } catch (IOException e) {
            throw new IOException("Şəkili yükləmək mümkün olmadı: " + e);
        }

        try {
            book.setName(name);
            book.setIsbn(isbn);
            book.setPublishedYear(publishedYear);
            book.setImage(imageUrl);
            book.setStockCount(stockCount);
            book.setAuthors(authors);
            book.setCategory(category);
            book.setStudents(students);
            book.setOrders(orders);
            bookRepository.save(book);
            BookDTO bookDTO=bookMapper.bookToBookDTO(book);
            log.info("Successfully updated{}", book);
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when updating book!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();
            if (books == null || books.isEmpty()) {
                throw new NoSuchElementException("Not found books!");
            }
            List<BookDTO> bookDTOS = new ArrayList<>();
            for (Book book : books) {
                BookDTO bookDTO = bookMapper.bookToBookDTO(book);
                bookDTOS.add(bookDTO);
            }
            log.info("Successfully retrieved{}", bookDTOS);
            return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving books!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BookDTO> getBookById(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found book by id=" + id));
            BookDTO bookDTO = bookMapper.bookToBookDTO(book);
            log.info("Successfully retrieved{}", bookDTO);
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving book by id=" + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> delete(Long id) {
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found book by id=" + id));

            for (Order order: book.getOrders()){
                order.getBooks().remove(book);
            }
            orderRepository.saveAll(book.getOrders());

            bookRepository.delete(book);
            log.info("Successfully deleted{}", book);
            return new ResponseEntity<>("Successfully deleted{" + book + "}", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occured while deleting book by id=" + id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
