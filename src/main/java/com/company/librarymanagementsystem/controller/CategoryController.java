package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.request.CategoryRequest;
import com.company.librarymanagementsystem.service.inter.CategoryServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceInter categoryServiceInter;

    @GetMapping("/")
    String getCategoryPage(){
        return "category";
    }

    @PostMapping("/save/{bookIds}")
    public ResponseEntity<Category> save(@RequestBody CategoryRequest categoryRequest, @PathVariable String bookIds) {
        try {
            List<Long> bookIdList = Arrays.stream(bookIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            Category savedCategory = categoryServiceInter.save(categoryRequest, bookIdList).getBody();

            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}/{name}/{bookIds}")
    ResponseEntity<Category> update(@PathVariable Long id, @PathVariable String name, @PathVariable String bookIds){
        try {
            List<Long> bookIdList = Arrays.stream(bookIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            Category categoryUpdate = categoryServiceInter.update(id, name, bookIdList).getBody();
            return ResponseEntity.ok(categoryUpdate);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories")
    ResponseEntity<List<CategoryDTO>> getAllCategory() {
        List<CategoryDTO> categoryDTOS = categoryServiceInter.getAllCategory().getBody();

        if (categoryDTOS == null || categoryDTOS.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(categoryDTOS);
    }

   @GetMapping("/getById/{id}")
   ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id,Model model){
        CategoryDTO categoryDTO=categoryServiceInter.getCategoryById(id).getBody();
        model.addAttribute("categoryDTO",categoryDTO);
        return ResponseEntity.ok(categoryDTO);
   }

   @DeleteMapping("/delete/{id}")
   ResponseEntity<String> delete(@PathVariable Long id,Model model){
       categoryServiceInter.delete(id);
       model.addAttribute("successMessage", "Category successfully deleted!");
       return ResponseEntity.ok("Category successfully deleted!");
    }
}
