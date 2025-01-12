package com.company.librarymanagementsystem.request;

import com.company.librarymanagementsystem.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorRequest {
    private String name;
    private String surname;
    private List<Book> books;
}
