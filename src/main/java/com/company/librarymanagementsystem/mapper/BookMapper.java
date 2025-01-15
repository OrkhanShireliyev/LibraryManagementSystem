package com.company.librarymanagementsystem.mapper;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.request.BookRequest;
import org.mapstruct.Mapper;

@Mapper
public interface BookMapper {

    Book bookRequestToBook(BookRequest BookRequest);

    BookDTO bookToBookDTO(Book book);
}
