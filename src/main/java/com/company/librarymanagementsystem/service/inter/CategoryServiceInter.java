package com.company.librarymanagementsystem.service.inter;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.request.CategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryServiceInter {

    ResponseEntity<Category> save(CategoryRequest categoryRequest, List<Long> bookIds);

    ResponseEntity<Category> update(Long id,String name,List<Long> bookId);

    ResponseEntity<List<CategoryDTO>> getAllCategory();

    ResponseEntity<CategoryDTO> getCategoryById(Long id);

    ResponseEntity<String> delete(Long id);
}
