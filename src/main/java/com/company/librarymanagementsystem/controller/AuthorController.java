package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.service.inter.AuthorServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorServiceInter authorServiceInter;

    @GetMapping("/")
    public String getAuthorsPage() {
        return "author";
    }

    @PostMapping("/save/{bookId}")
    ResponseEntity<AuthorRequest> save(@RequestBody AuthorRequest authorRequest, @PathVariable String bookId, Model model){
        List<Long> bookListId=Arrays.stream(bookId.split(","))
                        .map(Long::parseLong)
                                .collect(Collectors.toList());
        AuthorRequest author=authorServiceInter.save(authorRequest,bookListId).getBody();
        model.addAttribute("author",author);
        return ResponseEntity.ok(author);
    }

    @PostMapping("/update/{id}/{name}/{surname}/{bookId}")
    String update(@PathVariable Long id,
                  @PathVariable String name,
                  @PathVariable String surname,
                  @PathVariable String bookId,
                  Model model){

        List<Long> bookListId= Arrays.stream(bookId.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Author authorUpdate= authorServiceInter.update(id,name,surname,bookListId).getBody();
        model.addAttribute("authorUpdate",authorUpdate);
        return "author";
    }

    @GetMapping("/authors")
    @ResponseBody
    List<AuthorDTO> getAllAuthors(){
        List<AuthorDTO> authors=authorServiceInter.getAllAuthors().getBody();
        return authors;
    }

    @GetMapping("/getById/{id}")
    String getAuthorById(@PathVariable Long id,Model model){
        AuthorDTO authorDTO=authorServiceInter.getAuthorById(id).getBody();
        model.addAttribute("authorDTO",authorDTO);
        return "author";
    }

    @PostMapping("/delete/{id}")
    String delete(@PathVariable Long id,Model model){
        authorServiceInter.delete(id);
        model.addAttribute("successMessage", "Author successfully deleted!");
        model.addAttribute("authors", authorServiceInter.getAllAuthors());
        return "author";
    }
}
