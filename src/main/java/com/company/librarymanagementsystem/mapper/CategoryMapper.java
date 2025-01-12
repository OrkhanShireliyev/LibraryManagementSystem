package com.company.librarymanagementsystem.mapper;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.request.CategoryRequest;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

    Category categoryRequestToCategory(CategoryRequest categoryRequest);

    CategoryDTO categoryToCategoryDTO(Category category);
}
