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

    @GetMapping("/")
    String getCategoryPage(){
        return "category";
    }
    @PostMapping("/save")
    String save(@ModelAttribute CategoryRequest categoryRequest){
        categoryServiceInter.save(categoryRequest);
        return "category";
    }

    @PostMapping("/update/{id}/{name}/{bookId}")
    String update(@PathVariable Long id, @PathVariable String name,List<Long> bookId, Model model){
        Category categoryUpdate=categoryServiceInter.update(id,name,bookId).getBody();
        model.addAttribute("categoryUpdate",categoryUpdate);
        return "category";
    }

    @GetMapping("/categories")
    String getAllCategory(Model model){
        List<CategoryDTO> categoryDTOS=categoryServiceInter.getAllCategory().getBody();
        model.addAttribute("categoryDTOS",categoryDTOS);
        return "category";
    }

   @GetMapping("/getById/{id}")
   String getCategoryById(@PathVariable Long id,Model model){
        CategoryDTO categoryDTO=categoryServiceInter.getCategoryById(id).getBody();
        model.addAttribute("categoryDTO",categoryDTO);
        return "category";
   }

   @PostMapping("/delete/{id}")
   String delete(@PathVariable Long id,Model model){
       categoryServiceInter.delete(id);
       model.addAttribute("successMessage", "Category successfully deleted!");
       return "category";
    }
}
