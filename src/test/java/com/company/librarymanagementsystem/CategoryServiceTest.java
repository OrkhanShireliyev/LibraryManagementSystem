package com.company.librarymanagementsystem;

import com.company.librarymanagementsystem.dto.CategoryDTO;
import com.company.librarymanagementsystem.mapper.CategoryMapper;
import com.company.librarymanagementsystem.model.*;
import com.company.librarymanagementsystem.repository.*;
import com.company.librarymanagementsystem.request.CategoryRequest;
import com.company.librarymanagementsystem.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    void testSaveCategory_Success() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Science");

        Category category = new Category();
        category.setId(1L);
        category.setName("Science");
        category.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(1L);
        book.setName("Physics");
        book.setCategory(null);

        List<Book> books = List.of(book);

        when(bookRepository.findAllById(List.of(1L))).thenReturn(books);
        when(categoryMapper.categoryRequestToCategory(categoryRequest)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryServiceImpl.save(categoryRequest, List.of(1L));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Science", response.getBody().getName());
        verify(bookRepository).findAllById(List.of(1L));
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testSaveCategory_BooksNotFound() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("History");

        when(bookRepository.findAllById(List.of(1L))).thenReturn(List.of());

        ResponseEntity<Category> response = categoryServiceImpl.save(categoryRequest, List.of(1L));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(bookRepository).findAllById(List.of(1L));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testSaveCategory_ExceptionThrown() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Math");

        when(bookRepository.findAllById(anyList())).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<Category> response = categoryServiceImpl.save(categoryRequest, List.of(1L));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateCategoryWithValidData() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Old Name");
        category.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");

        List<Book> books = List.of(book);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookRepository.findAllById(List.of(1L))).thenReturn(books);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryServiceImpl.update(1L, "Updated Name", List.of(1L));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
        assertEquals(1, response.getBody().getBooks().size());
        assertEquals(book, response.getBody().getBooks().get(0));

        verify(categoryRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findAllById(List.of(1L));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testGetAllCategory_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Science");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Science");

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.categoryToCategoryDTO(category)).thenReturn(categoryDTO);

        ResponseEntity<List<CategoryDTO>> response = categoryServiceImpl.getAllCategory();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Science", response.getBody().get(0).getName());

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).categoryToCategoryDTO(category);
    }

    @Test
    void testGetAllCategory_NotFound() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CategoryDTO>> response = categoryServiceImpl.getAllCategory();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Science");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Science");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.categoryToCategoryDTO(category)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryServiceImpl.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Science", response.getBody().getName());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).categoryToCategoryDTO(category);
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenThrow(new NoSuchElementException("Not found category by id=1"));

        ResponseEntity<CategoryDTO> response = categoryServiceImpl.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, never()).categoryToCategoryDTO(any());
    }

    @Test
    void testDeleteCategory_Success() {
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        category.setName("Fiction");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(id);

        ResponseEntity<String> response = categoryServiceImpl.delete(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted category!", response.getBody());

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCategory_NotFound() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenThrow(new NoSuchElementException("Not found category by id=" + id));

        ResponseEntity<String> response = categoryServiceImpl.delete(id);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred when deleting category!", response.getBody());

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, never()).deleteById(any());
    }
}
