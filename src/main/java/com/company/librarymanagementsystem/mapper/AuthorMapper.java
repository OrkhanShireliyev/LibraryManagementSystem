package com.company.librarymanagementsystem.mapper;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.request.AuthorRequest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author authorRequestToAuthor(AuthorRequest authorRequest);
    AuthorDTO authorToAuthorDTO(Author author);

}
