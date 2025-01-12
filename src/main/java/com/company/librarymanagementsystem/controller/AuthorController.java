package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.service.inter.AuthorServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorServiceInter authorServiceInter;

    @PostMapping("/save")
    String save(@ModelAttribute AuthorRequest authorRequest){
        authorServiceInter.save(authorRequest);
        return "save";
    }

    @PostMapping("/update/{id}/{name}/{surname}")
    String update(@PathVariable Long id,@PathVariable String name,@PathVariable String surname, Model model){
        Author authorUpdate= authorServiceInter.update(id,name,surname).getBody();
        model.addAttribute("authorUpdate",authorUpdate);
        return "update";
    }

    @GetMapping("/authors")
    String getAllAuthors(Model model){
        List<AuthorDTO> authorDTOS=authorServiceInter.getAllAuthors().getBody();
        model.addAttribute("authorDTOS",authorDTOS);
        return "authors";
    }

    @GetMapping("/getById/{id}")
    String getAuthorById(@PathVariable Long id,Model model){
        AuthorDTO authorDTO=authorServiceInter.getAuthorById(id).getBody();
        model.addAttribute("authorDTO",authorDTO);
        return "getById";
    }

    @PostMapping("/delete/{id}")
    String delete(@PathVariable Long id,Model model){
        authorServiceInter.delete(id);
        model.addAttribute("successMessage", "Author successfully deleted!");
        return "delete";
    }
}
