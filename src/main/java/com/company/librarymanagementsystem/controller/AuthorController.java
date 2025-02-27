package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.mapper.AuthorMapper;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.service.inter.AuthorServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final AuthorMapper authorMapper;

    @GetMapping("/")
    public String getAuthorsPage() {
        return "author";
    }

    @PostMapping("/save/{bookId}")
    ResponseEntity<AuthorDTO> save(@RequestBody AuthorRequest authorRequest, @PathVariable String bookId, Model model){
        List<Long> bookListId=Arrays.stream(bookId.split(","))
                        .map(Long::parseLong)
                                .collect(Collectors.toList());
        ResponseEntity<AuthorRequest> authorRequest1=authorServiceInter.save(authorRequest,bookListId);
        if (authorRequest1.getBody() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        AuthorRequest author = authorRequest1.getBody();
        Author author1 = authorMapper.authorRequestToAuthor(author);
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author1);
        model.addAttribute("author",author);
        return ResponseEntity.ok(authorDTO);
    }

    @PutMapping("/update/{id}/{name}/{surname}/{bookId}")
    ResponseEntity<AuthorDTO> update(@PathVariable Long id,
                  @PathVariable String name,
                  @PathVariable String surname,
                  @PathVariable String bookId,
                  Model model){

        List<Long> bookListId= Arrays.stream(bookId.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Author authorUpdate= authorServiceInter.update(id,name,surname,bookListId).getBody();
        model.addAttribute("authorUpdate",authorUpdate);
        AuthorDTO authorDTO=authorMapper.authorToAuthorDTO(authorUpdate);
        return ResponseEntity.ok(authorDTO);
    }

    @GetMapping("/authors")
    ResponseEntity<List<AuthorDTO>> getAllAuthors(){
        List<AuthorDTO> authors=authorServiceInter.getAllAuthors().getBody();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id, Model model){
        AuthorDTO authorDTO=authorServiceInter.getAuthorById(id).getBody();
        model.addAttribute("authorDTO",authorDTO);
        return ResponseEntity.ok(authorDTO);
    }

    @PostMapping("/delete/{id}")
    String delete(@PathVariable Long id,Model model){
        authorServiceInter.delete(id);
        model.addAttribute("successMessage", "Author successfully deleted!");
        model.addAttribute("authors", authorServiceInter.getAllAuthors());
        return "author";
    }
}
