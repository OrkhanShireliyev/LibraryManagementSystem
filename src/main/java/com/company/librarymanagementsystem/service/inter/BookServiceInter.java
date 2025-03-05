package com.company.librarymanagementsystem.service.inter;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.request.BookRequest;
import com.company.librarymanagementsystem.request.StudentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface BookServiceInter {

    ResponseEntity<BookDTO> save(String isbn,
                                     String name,
                                     String publishedYear,
                                     Long stockCount,
                                     MultipartFile image,
                                     List<Long> authorId,
                                     List<Long> studentId,
                                     Long categoryId,
                                     List<Long> orderId) throws IOException;

    ResponseEntity<BookDTO> update(Long id,
                                String name,
                                String isbn,
                                String publishedYear,
                                MultipartFile image,
                                Long stockCount,
                                List<Long> authorId,
                                Long categoryId,
                                List<Long> studentId,
                                List<Long> orderId
                                ) throws IOException;

    ResponseEntity<List<BookDTO>> getAllBooks();

    ResponseEntity<BookDTO> getBookById(Long id);

    ResponseEntity<String> delete(Long id);
}
