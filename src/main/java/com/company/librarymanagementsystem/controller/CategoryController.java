package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.request.CategoryRequest;
import com.company.librarymanagementsystem.service.inter.CategoryServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceInter categoryServiceInter;

    @PostMapping("/save")
    String save(@ModelAttribute CategoryRequest categoryRequest){
        categoryServiceInter.save(categoryRequest);
        return "save";
    }

    @PostMapping("/update/{id}/{name}")
    String update(@PathVariable Long id, @PathVariable String name, Model model){
        Category categoryUpdate=categoryServiceInter.update(id,name).getBody();
        model.addAttribute("categoryUpdate",categoryUpdate);
        return "update";
    }

    @GetMapping("/categories")
    String getAllCategory(Model model){
        List<CategoryDTO> categoryDTOS=categoryServiceInter.getAllCategory().getBody();
        model.addAttribute("categoryDTOS",categoryDTOS);
        return "categories";
    }

   @GetMapping("/getById/{id}")
   String getCategoryById(@PathVariable Long id,Model model){
        CategoryDTO categoryDTO=categoryServiceInter.getCategoryById(id).getBody();
        model.addAttribute("categoryDTO",categoryDTO);
        return "getById";
   }

   @PostMapping("/delete/{id}")
   String delete(@PathVariable Long id,Model model){
        categoryServiceInter.delete(id);
       model.addAttribute("successMessage", "Author successfully deleted!");
       return "delete";
    }
}
