package com.company.librarymanagementsystem.swagger;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.mapper.AuthorMapper;
import com.company.librarymanagementsystem.model.Author;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.service.inter.AuthorServiceInter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Author Controller", description = "Operations related to author management")
public class AuthorControllerSwagger {
    private final AuthorServiceInter authorServiceInter;
    private final AuthorMapper authorMapper;

    @Operation(summary = "Save authors!", description = "Fill author information and save it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved!"),
            @ApiResponse(responseCode = "500", description = "Can't save author!")
    })
    @PostMapping("/save/{bookId}")
    ResponseEntity<AuthorRequest> save(@RequestBody AuthorRequest authorRequest, @PathVariable List<Long> bookId) {
        try {
            AuthorRequest author = authorServiceInter.save(authorRequest, bookId).getBody();
            return new ResponseEntity(author, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Update author!", description = "Fill author changing and update it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated!"),
            @ApiResponse(responseCode = "500", description = "Can't update author!")
    })
    @PutMapping("/update/{id}/{name}/{surname}/{bookId}")
    ResponseEntity<AuthorDTO> update(@PathVariable Long id,
                                     @PathVariable String name,
                                     @PathVariable String surname,
                                     @PathVariable List<Long> bookId
    ) {
        try {
            Author authorUpdate = authorServiceInter.update(id, name, surname, bookId).getBody();
            AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(authorUpdate);
            return new ResponseEntity(authorDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all authors", description = "Get all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The authors were not found")
    })
    @GetMapping("/authors")
    ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        try {
            List<AuthorDTO> authors = authorServiceInter.getAllAuthors().getBody();
            return ResponseEntity.status(HttpStatus.OK).body(authors);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a author by id", description = "Returns a author as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The author was not found")
    })
    @GetMapping("/getById/{id}")
    ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        try {
            AuthorDTO authorDTO = authorServiceInter.getAuthorById(id).getBody();
            return ResponseEntity.status(HttpStatus.OK).body(authorDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a book by id", description = "Delete a book as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Can't delete author!")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            authorServiceInter.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted!");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
