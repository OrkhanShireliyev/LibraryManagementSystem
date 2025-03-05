package com.company.librarymanagementsystem.dto;

import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.model.Category;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String name;
    private String isbn;
    private String publishedYear;
    private String image;
    private Long stockCount;
    private List<Author> authors;
    private Category category;
    private List<Student> students;
    private List<Order> orders;
}
