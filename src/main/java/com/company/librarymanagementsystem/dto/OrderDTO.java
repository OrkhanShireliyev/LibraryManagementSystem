package com.company.librarymanagementsystem.dto;

import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private Long id;

    private Long orderNumber;

    private LocalDate localDate;

    private LocalDate deliveryTime;

    private List<Book> books;

    private Student student;
}
