package com.company.librarymanagementsystem.request;

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
public class OrderRequest {
    private Long orderNumber;

    private LocalDate localDate;
}
