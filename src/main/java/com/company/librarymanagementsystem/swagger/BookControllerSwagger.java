package com.company.librarymanagementsystem.swagger;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.service.inter.BookServiceInter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "Operations related to book management")
public class BookControllerSwagger {

    private final BookServiceInter bookServiceInter;

    @Operation(summary = "Save book", description = "Fill book information and save it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved!"),
            @ApiResponse(responseCode = "500", description = "Can't save book!")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<BookDTO> save(@RequestParam("name") String name,
                                 @RequestParam("isbn") String isbn,
                                 @RequestParam("publishedYear") String publishedYear,
                                 @RequestParam("image") MultipartFile image,
                                 @RequestParam("stockCount") Long stockCount,
                                 @RequestParam("authors") List<Long> authorId,
                                 @RequestParam("category") Long categoryId,
                                 @RequestParam("students") List<Long> studentId,
                                 @RequestParam("order") List<Long> orderNumber
    ) {
        try {
            BookDTO bookDTO = bookServiceInter.save(isbn, name, publishedYear, stockCount,
                    image, authorId, studentId, categoryId, orderNumber).getBody();
            return new ResponseEntity<>(bookDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update book", description = "Fill book for change book's info and update it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated!"),
            @ApiResponse(responseCode = "500", description = "Can't update book!")
    })
    @PutMapping(value = "/update/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<BookDTO> update(
            @PathVariable("bookId") Long id,
            @RequestParam("name") String name,
            @RequestParam("isbn") String isbn,
            @RequestParam("publishedYear") String publishedYear,
            @RequestParam("stockCount") Long stockCount,
            @RequestParam(value = ("image"), required = false) MultipartFile image,
            @RequestParam("authors") List<Long> authorId,
            @RequestParam("students") List<Long> studentId,
            @RequestParam("category") Long categoryId,
            @RequestParam("order") List<Long> orderId
    ) {
        try {
            BookDTO book = bookServiceInter.update(id, name, isbn, publishedYear, image, stockCount,
                    authorId, categoryId, studentId, orderId).getBody();
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all books", description = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The book was not found")
    })
    @GetMapping("/books")
    ResponseEntity<List<BookDTO>> getAllBooks() {
        try {
            List<BookDTO> bookDTOS = bookServiceInter.getAllBooks().getBody();
            return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a book by id", description = "Returns a book as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The book was not found")
    })
    @GetMapping("/getById/{id}")
    ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        try {
            BookDTO bookDTO = bookServiceInter.getBookById(id).getBody();
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a book by id", description = "Delete a book as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Can't delete book!")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            bookServiceInter.delete(id);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occured deleting book!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
