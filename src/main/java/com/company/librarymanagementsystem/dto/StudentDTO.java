package com.company.librarymanagementsystem.dto;

import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String registryCode;
    private String name;
    private String surname;
    private int age;
    private List<Book> books;
    private List<Order> orders;
}
