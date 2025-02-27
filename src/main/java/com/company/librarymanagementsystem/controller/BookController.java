package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.request.BookRequest;
import com.company.librarymanagementsystem.service.inter.BookServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookServiceInter bookServiceInter;

    @GetMapping("/")
    String getBooksPage() {
        return "book";
    }

    @PostMapping("/save")
    ResponseEntity<BookDTO> save(@RequestParam("name") String name,
                                 @RequestParam("isbn") String isbn,
                                 @RequestParam("publishedYear") String publishedYear,
                                 @RequestParam("image") MultipartFile image,
                                 @RequestParam("stockCount") Long stockCount,
                                 @RequestParam("authors") List<Long> authorId,
                                 @RequestParam("category") Long categoryId,
                                 @RequestParam("students") List<Long> studentId,
                                 @RequestParam("order") List<Long> orderId
                                , Model model) throws IOException {
        BookDTO bookDTO = bookServiceInter.save(isbn, name, publishedYear, stockCount, image, authorId, studentId, categoryId, orderId).getBody();
        model.addAttribute("bookDTO", bookDTO);
        return ResponseEntity.ok(bookDTO);
    }

    @PostMapping(value = "/update/{bookId}")
    ResponseEntity<BookDTO> update(
            @PathVariable("bookId") Long id,
            @RequestParam("name") String name,
            @RequestParam("isbn") String isbn,
            @RequestParam("publishedYear") String publishedYear,
            @RequestParam("stockCount") Long stockCount,
            @RequestParam(value = ("image"), required = false) MultipartFile image,
            @RequestParam("authors") List<Long> authorId,
            @RequestParam("students") List<Long> studentId,
            @RequestParam("category") Long categoryId,
            @RequestParam("order") List<Long> orderId
    ) throws IOException {
        BookDTO book = bookServiceInter.update(id, name, isbn, publishedYear, image, stockCount, authorId, categoryId, studentId, orderId).getBody();
        System.out.println(book);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/books")
    ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTOS = bookServiceInter.getAllBooks().getBody();
        return ResponseEntity.ok(bookDTOS);
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<BookDTO> getBookById(@PathVariable Long id, Model model) {
        BookDTO bookDTO = bookServiceInter.getBookById(id).getBody();
        model.addAttribute("bookDTO", bookDTO);
        return ResponseEntity.ok(bookDTO);
    }

    @PostMapping("/delete/{id}")
    String delete(@PathVariable Long id, Model model) {
        bookServiceInter.delete(id);
        model.addAttribute("successMessage", "Student successfully deleted!");
        model.addAttribute("authors", bookServiceInter.getAllBooks());
        return "book";
    }
}
