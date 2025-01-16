package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.request.BookRequest;
import com.company.librarymanagementsystem.service.inter.BookServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookServiceInter bookServiceInter;

    @PostMapping("/save")
    String save(@ModelAttribute BookRequest bookRequest){
        bookServiceInter.save(bookRequest);
        return "save";
    }

    @PostMapping("/update/{id}/{name}/{isbn}/{publishedYear}/{image}/{stockCount}/{authorId}/{categoryId}/{studentId}")
    String update(@PathVariable Long id,
                  @PathVariable String name,
                  @PathVariable String isbn,
                  @PathVariable String publishedYear,
                  @PathVariable String image,
                  @PathVariable Long stockCount,
                  @PathVariable List<Long> authorId,
                  @PathVariable Long categoryId,
                  @PathVariable List<Long> studentId,
                  Model model){
     Book book=bookServiceInter.update(id, name, isbn, publishedYear, image, stockCount, authorId, categoryId, studentId).getBody();
     model.addAttribute("update",book);
     return "update";
    }

    @GetMapping("/books")
    String getAllBooks(Model model){
        List<BookDTO> bookDTOS=bookServiceInter.getAllBooks().getBody();
        model.addAttribute("books",bookDTOS);
        return "books";
    }

    @GetMapping("/getById/{id}")
    String getBookById(@PathVariable Long id,Model model){
        BookDTO bookDTO=bookServiceInter.getBookById(id).getBody();
        model.addAttribute("bookDTO",bookDTO);
        return "getById";
    }

    @PostMapping("/delete/{id}")
    String delete(@PathVariable Long id,Model model){
        bookServiceInter.delete(id);
        model.addAttribute("successMessage", "Student successfully deleted!");
        return "delete";
    }
}
