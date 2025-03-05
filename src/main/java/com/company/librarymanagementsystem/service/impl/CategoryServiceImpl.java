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
    public ResponseEntity<Category> save(CategoryRequest categoryRequest, List<Long> bookIds) {
        try {
            List<Book> books = bookRepository.findAllById(bookIds);
            if (books.isEmpty()) {
                throw new NoSuchElementException("Not found books!");
            }
            Category category = categoryMapper.categoryRequestToCategory(categoryRequest);

            if (category.getBooks() == null) {
                category.setBooks(new ArrayList<>());
            }

            for (Book book : books) {
                if (book.getCategory() != null) {
                    book.getCategory().getBooks().remove(book);
                }
                book.setCategory(category);
                log.info("The book has been assigned to a new category: {}", book.getName());
            }
            category.getBooks().addAll(books);
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (Exception e) {
            log.error("Error occurred when creating category: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Category> update(Long id, String name, List<Long> bookIds) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Not found category by id=" + id));

            List<Book> books = bookRepository.findAllById(bookIds);
            if (books.isEmpty()) {
                throw new RuntimeException("Not found books!");
            }

            for (Book oldBook : category.getBooks()) {
                oldBook.setCategory(null);
            }
            category.getBooks().clear();

            for (Book book : books) {
                book.setCategory(category);
                log.info("The book has been assigned to a new category: {}", book.getName());
            }

            category.setBooks(books);
            category.setName(name);

            categoryRepository.save(category);
            log.info("Successfully updated{}", category);
            return new ResponseEntity<>(category, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred when updating category by id=" + id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories==null || categories.isEmpty()) {
                throw new NoSuchElementException("Not found categories!");
            }
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            for (Category category : categories) {
                CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
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
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(()->new NoSuchElementException("Not found category by id=" + id));

            CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);

            log.info("Successfully retrieved{}", categoryDTO);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when retrieving category by id="+id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(()->new NoSuchElementException("Not found category by id=" + id));

            categoryRepository.deleteById(id);
            log.info("Successfully deleted category!");
            return new ResponseEntity<>("Successfully deleted category!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when deleting category by id=" + id);
            return new ResponseEntity<>("Error occurred when deleting category!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
