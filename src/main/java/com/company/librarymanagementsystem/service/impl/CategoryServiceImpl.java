package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.mapper.CategoryMapper;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.repository.CategoryRepository;
import com.company.librarymanagementsystem.request.CategoryRequest;
import com.company.librarymanagementsystem.service.inter.CategoryServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryServiceInter {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public ResponseEntity<CategoryRequest> save(CategoryRequest categoryRequest) {
        try {
            Category category = categoryMapper.categoryRequestToCategory(categoryRequest);
            categoryRepository.save(category);
            log.info("Successfully created{}", category);
            return new ResponseEntity<>(categoryRequest, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when creating category!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Category> update(Long id, String name) {
        Category category=categoryRepository.findById(id).get();
        if (category==null){
            throw new NoSuchElementException("");
        }


        return null;
    }

    @Override
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        return null;
    }

    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<String> delete(Long id) {

    }
}
