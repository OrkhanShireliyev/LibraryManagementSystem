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

import java.util.List;

@Service
public interface BookServiceInter {

    ResponseEntity<BookRequest> save(BookRequest bookRequest);

    ResponseEntity<Book> update(Long id,
                                String name,
                                String isbn,
                                String publishedYear,
                                String image,
                                Long stockCount,
                                List<Long> authorId,
                                Long categoryId,
                                List<Long> studentId);

    ResponseEntity<List<BookDTO>> getAllBooks();

    ResponseEntity<BookDTO> getBookById(Long id);

    ResponseEntity<String> delete(Long id);
}
