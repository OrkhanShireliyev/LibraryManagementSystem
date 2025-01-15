package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.mapper.CategoryMapper;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.repository.BookRepository;
import com.company.librarymanagementsystem.repository.CategoryRepository;
import com.company.librarymanagementsystem.request.CategoryRequest;
import com.company.librarymanagementsystem.service.inter.CategoryServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryServiceInter {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final BookRepository bookRepository;

    @Override
    public ResponseEntity<CategoryRequest> save(CategoryRequest categoryRequest) {
        try {
            Category category = categoryMapper.categoryRequestToCategory(categoryRequest);
            categoryRepository.save(category);
            log.info("Successfully created{}", category);
            return new ResponseEntity<>(categoryRequest, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when creating category!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Category> update(Long id, String name,List<Long> bookId) {
        Category category = categoryRepository.findById(id).get();
        if (category == null) {
            throw new NoSuchElementException("Not found category by id=" + id);
        }

        List<Book> books=new ArrayList<>();
        for (Long bookById:bookId){
            Book findBookById=bookRepository.findById(bookById).get();
            if (findBookById==null){
                throw new NoSuchElementException("Not found book by id="+bookById);
            }
            books.add(findBookById);
        }

        try {
            category.setName(name);
            categoryRepository.save(category);
            log.info("Successfully updated{}", category);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when updating category{}",category);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        if (categories == null) {
            throw new NullPointerException("Not found categories!");
        }

        try {
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            CategoryDTO categoryDTO;
            for (Category category : categories) {
                categoryDTO = categoryMapper.categoryToCategoryDTO(category);
                categoryDTOS.add(categoryDTO);
            }
            log.info("Successfully retrieved{}", categoryDTOS);
            return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving categories!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<CategoryDTO> getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).get();
        if (category == null) {
            throw new NoSuchElementException("Not found category by id=" + id);
        }
        try {
            CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
            log.info("Successfully retrieved{}", categoryDTO);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving category{}",category);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        Category category = categoryRepository.findById(id).get();
        if (category == null) {
            throw new NoSuchElementException("Not found category by id=" + id);
        }
        try {
            categoryRepository.deleteById(id);
            log.info("Successfully deleted{}", category);
            return new ResponseEntity<>("Successfully deleted{"+category+"}", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when deleting category by id=" + id);
            return new ResponseEntity<>("Error occurred when deleting{"+category+"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
