package com.company.librarymanagementsystem.swagger;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.request.CategoryRequest;
import com.company.librarymanagementsystem.service.inter.CategoryServiceInter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Operations related to category management")
public class CategoryControllerSwagger {

    private final CategoryServiceInter categoryServiceInter;

    @Operation(summary = "Save category", description = "Fill category information and save it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved!"),
            @ApiResponse(responseCode = "500", description = "Can't save category!")
    })
    @PostMapping("/save/{bookIds}")
    ResponseEntity<Category> save(@RequestBody CategoryRequest categoryRequest, @PathVariable List<Long> bookIds) {
        try {
            Category savedCategory = categoryServiceInter.save(categoryRequest, bookIds).getBody();
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Update category", description = "Fill category for change category's info and update it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated!"),
            @ApiResponse(responseCode = "500", description = "Can't update category!")
    })
    @PutMapping("/update/{id}/{name}/{bookIds}")
    ResponseEntity<Category> update(@PathVariable Long id, @PathVariable String name, @PathVariable List<Long> bookIds) {
        try {
            Category categoryUpdate = categoryServiceInter.update(id, name, bookIds).getBody();
            return new ResponseEntity<>(categoryUpdate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all categories", description = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The category was not found")
    })
    @GetMapping("/categories")
    ResponseEntity<List<CategoryDTO>> getAllCategory() {
        try {
            List<CategoryDTO> categoryDTOS = categoryServiceInter.getAllCategory().getBody();
            if (categoryDTOS == null || categoryDTOS.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a category by id", description = "Returns a category as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The category was not found")
    })
    @GetMapping("/getById/{id}")
    ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            CategoryDTO categoryDTO = categoryServiceInter.getCategoryById(id).getBody();
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a category by id", description = "Delete a category as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Can't delete category!")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            categoryServiceInter.delete(id);
            return new ResponseEntity<>("Category successfully deleted!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred deleting category!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
