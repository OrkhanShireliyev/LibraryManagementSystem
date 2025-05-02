package com.company.librarymanagementsystem;

import com.company.librarymanagementsystem.dto.AuthorDTO;
import com.company.librarymanagementsystem.mapper.AuthorMapper;
import com.company.librarymanagementsystem.model.*;
import com.company.librarymanagementsystem.repository.*;
import com.company.librarymanagementsystem.request.AuthorRequest;
import com.company.librarymanagementsystem.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorMapper authorMapper;
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorServiceImpl;


    @Test
    void testSaveAuthorValidData() {
        Student student = new Student();
        student.setId(1L);
        student.setRegistryCode("123456");
        student.setName("Vasif");
        student.setSurname("Balayev");
        student.setAge(22);

        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber(10L);
        order.setLocalDate(LocalDate.of(2025, 2, 18));
        order.setDeliveryTime(LocalDate.of(2025, 3, 1));

        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");

        Book book = new Book();
        book.setId(1L);
        book.setIsbn("hry66fhd7j");
        book.setName("The da Vinci Code");
        book.setStockCount(10L);
        book.setPublishedYear("1987");
        book.setStudents(List.of(student));
        book.setCategory(category);
        book.setOrders(List.of(order));
        book.setImage("https://s3.com/test.jpg");

        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setName("Orkhan");
        authorRequest.setSurname("Shireliyev");

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(bookRepository.findAllById(anyList())).thenReturn(List.of(book));

        ResponseEntity<AuthorRequest> authorRequestResponse = authorServiceImpl.save(authorRequest, List.of(1L));

        assertNotNull(authorRequestResponse, "Response should not be null");
        assertEquals(HttpStatus.OK, authorRequestResponse.getStatusCode(), "Response should be OK");
        assertNotNull(authorRequestResponse.getBody(), "Author should not be null");
        assertEquals("Orkhan", authorRequestResponse.getBody().getName());
        assertEquals("Shireliyev", authorRequestResponse.getBody().getSurname());

        verify(authorRepository, times(1)).save(any());
    }


    @Test
    void testUpdateAuthorWithValidData() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Orkhan");
        author.setSurname("Shireliyev");

        Student student = new Student();
        student.setId(1L);
        student.setRegistryCode("123456");
        student.setName("Vasif");
        student.setSurname("Balayev");
        student.setAge(22);

        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber(10L);
        order.setLocalDate(LocalDate.of(2025, 2, 18));
        order.setDeliveryTime(LocalDate.of(2025, 3, 1));

        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");

        Book book = new Book();
        book.setId(1L);
        book.setIsbn("hry66fhd7j");
        book.setName("The da Vinci Code");
        book.setStockCount(10L);
        book.setPublishedYear("1987");
        book.setAuthors(new ArrayList<>(List.of(author)));
        book.setStudents(List.of(student));
        book.setCategory(category);
        book.setOrders(List.of(order));
        book.setImage("https://s3.com/test.jpg");

        author.setBooks(new ArrayList<>(List.of(book)));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(bookRepository.findAllById(anyList())).thenReturn(List.of(book));
        when(bookRepository.saveAll(any())).thenReturn(List.of(book));

        ResponseEntity<Author> response = authorServiceImpl.update(
                1L, "Orkhan", "Shireliyev", List.of(1L));

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");
        assertNotNull(response.getBody(), "Author should not be null");
        assertEquals("Orkhan", response.getBody().getName());
        assertEquals("Shireliyev", response.getBody().getSurname());

        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(bookRepository, times(1)).findAllById(List.of(1L));
        verify(bookRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testGetAllAuthors() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Orkhan");
        author.setSurname("Shireliyev");

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Orkhan");
        authorDTO.setSurname("Shireliyev");

        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorMapper.authorToAuthorDTO(author)).thenReturn(authorDTO);

        ResponseEntity<List<AuthorDTO>> response = authorServiceImpl.getAllAuthors();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Orkhan", response.getBody().get(0).getName());
        assertEquals("Shireliyev", response.getBody().get(0).getSurname());

        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, times(1)).authorToAuthorDTO(author);
    }

    @Test
    void testGetAuthorById() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Orkhan");
        author.setSurname("Shireliyev");

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Orkhan");
        authorDTO.setSurname("Shireliyev");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.authorToAuthorDTO(author)).thenReturn(authorDTO);

        ResponseEntity<AuthorDTO> response = authorServiceImpl.getAuthorById(1L);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");
        assertNotNull(response.getBody(), "AuthorDTO should not be null");
        assertEquals("Orkhan", response.getBody().getName());
        verify(authorRepository).findById(1L);
        verify(authorMapper).authorToAuthorDTO(author);
    }

    @Test
    void testDeleteAuthor_Success() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Orkhan");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        ResponseEntity<String> response = authorServiceImpl.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Author deleted successfully", response.getBody());
        verify(authorRepository).findById(1L);
        verify(authorRepository).deleteById(1L);
    }

    @Test
    void testDeleteAuthor_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authorServiceImpl.delete(1L));
        verify(authorRepository).findById(1L);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAuthor_DeleteFails() {
        Author author = new Author();
        author.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        doThrow(new RuntimeException("DB error")).when(authorRepository).deleteById(1L);

        ResponseEntity<String> response = authorServiceImpl.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(authorRepository).findById(1L);
        verify(authorRepository).deleteById(1L);
    }
}
