package com.company.librarymanagementsystem;

import com.company.librarymanagementsystem.dto.BookDTO;
import com.company.librarymanagementsystem.mapper.BookMapper;
import com.company.librarymanagementsystem.model.*;
import com.company.librarymanagementsystem.repository.*;
import com.company.librarymanagementsystem.service.S3Service;
import com.company.librarymanagementsystem.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private S3Service s3Service;
    @Mock
    private MultipartFile image;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    private Book book1;
    private Book book2;
    private BookDTO bookDTO1;
    private BookDTO bookDTO2;

    @Test
    void testSaveBookWithValidData() throws IOException {
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

        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setIsbn("hry66fhd7j");
        expectedBook.setName("The da Vinci Code");
        expectedBook.setStockCount(10L);
        expectedBook.setPublishedYear("1987");
        expectedBook.setAuthors(List.of(author));
        expectedBook.setStudents(List.of(student));
        expectedBook.setCategory(category);
        expectedBook.setOrders(List.of(order));
        expectedBook.setImage("https://s3.com/test.jpg");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setIsbn("hry66fhd7j");
        bookDTO.setName("The da Vinci Code");
        bookDTO.setStockCount(10L);
        bookDTO.setPublishedYear("1987");
        bookDTO.setAuthors(List.of(author));
        bookDTO.setStudents(List.of(student));
        bookDTO.setCategory(category);
        bookDTO.setOrders(List.of(order));
        bookDTO.setImage("https://s3.com/test.jpg");

        when(authorRepository.findAllById(anyList())).thenReturn(List.of(author));
        when(studentRepository.findAllById(anyList())).thenReturn(List.of(student));
        when(orderRepository.findAllById(anyList())).thenReturn(List.of(order));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(expectedBook);

        when(image.getOriginalFilename()).thenReturn("test.jpg");
        when(image.getSize()).thenReturn(100L);
        when(image.getInputStream()).thenReturn(mock(InputStream.class));
        when(s3Service.uploadFile(any(), any(), any(), anyLong())).thenReturn("https://s3.com/test.jpg");

        ResponseEntity<BookDTO> response = bookServiceImpl.save(
                "hry66fhd7j", "The da Vinci Code", "1987", 10L, image,
                List.of(1L), List.of(1L), 1L, List.of(1L)
        );

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");
        assertNotNull(response.getBody(), "BookDTO should not be null");
        assertEquals("The da Vinci Code", response.getBody().getName());
        assertEquals("hry66fhd7j", response.getBody().getIsbn());
        assertEquals("1987", response.getBody().getPublishedYear());
        assertEquals(10, response.getBody().getStockCount());

        verify(bookRepository, times(1)).save(any(Book.class));
        verify(s3Service, times(1)).uploadFile(any(), any(), any(), anyLong());
        verify(authorRepository, times(1)).findAllById(anyList());
        verify(studentRepository, times(1)).findAllById(anyList());
        verify(categoryRepository, times(1)).findById(any());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateBookWithValidData() throws IOException {
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

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setIsbn("hry66fhd7j");
        updatedBook.setName("The da Vinci Code");
        updatedBook.setStockCount(10L);
        updatedBook.setPublishedYear("1987");
        updatedBook.setAuthors(List.of(author));
        updatedBook.setStudents(List.of(student));
        updatedBook.setCategory(category);
        updatedBook.setOrders(List.of(order));
        updatedBook.setImage("https://s3.com/test.jpg");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setIsbn("hry66fhd7j");
        bookDTO.setName("The da Vinci Code");
        bookDTO.setStockCount(10L);
        bookDTO.setPublishedYear("1987");
        bookDTO.setAuthors(List.of(author));
        bookDTO.setStudents(List.of(student));
        bookDTO.setCategory(category);
        bookDTO.setOrders(List.of(order));
        bookDTO.setImage("https://s3.com/test.jpg");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(orderRepository.findAllById(anyList())).thenReturn(List.of(order));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(updatedBook));
        when(bookMapper.bookToBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        when(image.getOriginalFilename()).thenReturn("test.jpg");
        when(image.getSize()).thenReturn(100L);
        when(image.getInputStream()).thenReturn(mock(InputStream.class));
        when(s3Service.uploadFile(any(), any(), any(), anyLong())).thenReturn("https://s3.com/test.jpg");

        ResponseEntity<BookDTO> response = bookServiceImpl.update(
                1L, "The da Vinci Code", "hry66fhd7j", "1987", image, 10L,
                List.of(1L), 1L, List.of(1L), List.of(1L)
        );

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");
        assertEquals("The da Vinci Code", response.getBody().getName());
        assertEquals("hry66fhd7j", response.getBody().getIsbn());
        assertEquals("1987", response.getBody().getPublishedYear());
        assertEquals(10, response.getBody().getStockCount());

        verify(bookRepository, times(1)).save(any(Book.class));
        verify(s3Service, times(1)).uploadFile(any(), any(), any(), anyLong());
        verify(authorRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(any());
        verify(orderRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).bookToBookDTO(updatedBook);

    }

    @BeforeEach
    void setUp() {
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

        book1 = new Book(1L, "The da Vinci Code", "hry66fhd7j", "1987", "https://s3.com/test1.jpg",
                10L, List.of(author), category, List.of(student), List.of(order));
        book2 = new Book(1L, "The da Vinci Code", "hry66fhd7j", "1987", "https://s3.com/test1.jpg",
                10L, List.of(author), category, List.of(student), List.of(order));

        bookDTO1 = new BookDTO(1L, "The da Vinci Code", "hry66fhd7j", "1987", "https://s3.com/test1.jpg",
                10L, List.of(author), category, List.of(student), List.of(order));
        bookDTO2 = new BookDTO(1L, "The da Vinci Code", "hry66fhd7j", "1987", "https://s3.com/test1.jpg",
                10L, List.of(author), category, List.of(student), List.of(order));
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));
        when(bookMapper.bookToBookDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.bookToBookDTO(book2)).thenReturn(bookDTO2);

        ResponseEntity<List<BookDTO>> response = bookServiceImpl.getAllBooks();

        assertEquals(2, response.getBody().size());
        assertEquals(bookDTO1, response.getBody().get(0));
        assertEquals(bookDTO2, response.getBody().get(1));

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");
        assertNotNull(response.getBody(), "BookDTO should not be null");

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(2)).bookToBookDTO(any(Book.class));
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book1));
        when(bookMapper.bookToBookDTO(book1)).thenReturn(bookDTO1);

        ResponseEntity<BookDTO> response = bookServiceImpl.getBookById(1L);

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response should be OK");

        assertNotNull(response.getBody(), "BookDTO should not be null");
        assertEquals(bookDTO1, response.getBody(), "Returned BookDTO should match expected");

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).bookToBookDTO(book1);
    }

    @Test
    void testDeleteBook_Success() {
        Book book = new Book();
        book.setId(1L);

        Order order = new Order();
        order.setId(1L);
        order.setBooks(new ArrayList<>(List.of(book)));

        book.setOrders(List.of(order));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<String> response = bookServiceImpl.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book deleted successfully", response.getBody());

        verify(orderRepository).saveAll(anyList());
        verify(bookRepository).delete(book);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = bookServiceImpl.delete(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteBook_DeleteFail() {
        Book book = new Book();
        book.setId(1L);
        book.setOrders(new ArrayList<>());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doThrow(new RuntimeException("DB error")).when(bookRepository).delete(book);

        ResponseEntity<String> response = bookServiceImpl.delete(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());

        verify(bookRepository).findById(1L);
        verify(bookRepository).delete(book);
    }
}

