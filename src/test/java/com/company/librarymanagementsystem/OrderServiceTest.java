package com.company.librarymanagementsystem;

import com.company.librarymanagementsystem.dto.OrderDTO;
import com.company.librarymanagementsystem.exception.NotFoundException;
import com.company.librarymanagementsystem.mapper.OrderMapper;
import com.company.librarymanagementsystem.model.*;
import com.company.librarymanagementsystem.repository.*;
import com.company.librarymanagementsystem.request.OrderRequest;
import com.company.librarymanagementsystem.service.S3Service;
import com.company.librarymanagementsystem.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrderMapper orderMapper;
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
    private OrderServiceImpl orderServiceImpl;

    @Test
    void testSaveOrder_Success() {
        Long studentId = 1L;
        Long bookId1 = 1L;
        Long bookId2 = 2L;
        List<Long> bookIds = List.of(bookId1, bookId2);

        Student student = new Student();
        student.setId(studentId);
        student.setRegistryCode("123");
        student.setName("John");
        student.setSurname("Doe");

        Book book1 = new Book();
        book1.setId(bookId1);
        book1.setName("Book 1");
        book1.setStockCount(5L);

        Book book2 = new Book();
        book2.setId(bookId2);
        book2.setName("Book 2");
        book2.setStockCount(3L);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderNumber(1L);

        when(bookRepository.findAllById(bookIds)).thenReturn(List.of(book1, book2));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(orderMapper.orderRequestToOrder(any(OrderRequest.class))).thenReturn(new Order());

        ResponseEntity<OrderRequest> response = orderServiceImpl.save(orderRequest, bookIds, studentId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(bookRepository, times(1)).findAllById(bookIds);
        verify(studentRepository, times(1)).findById(studentId);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(bookRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSaveOrder_BookNotFound() {
        Long studentId = 1L;
        Long bookId1 = 1L;
        List<Long> bookIds = List.of(bookId1);

        when(bookRepository.findAllById(bookIds)).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class, () -> {
            orderServiceImpl.save(new OrderRequest(), bookIds, studentId);
        });

        verify(bookRepository, times(1)).findAllById(bookIds);
    }

    @Test
    void testSaveOrder_OutOfStock() {
        Long studentId = 1L;
        Long bookId1 = 1L;
        List<Long> bookIds = List.of(bookId1);

        Book book = new Book();
        book.setId(bookId1);
        book.setName("Book 1");
        book.setStockCount(0L);

        when(bookRepository.findAllById(bookIds)).thenReturn(List.of(book));

        Student student = new Student();
        student.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Exception exception = assertThrows(ArithmeticException.class, () -> {
            orderServiceImpl.save(new OrderRequest(), bookIds, studentId);
        });

        String expectedMessage = "Book 1 book run out!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(bookRepository, times(1)).findAllById(bookIds);
        verify(studentRepository, times(1)).findById(studentId);
    }


    @Test
    void testReturnOrder_OrderNotFound() {
        Long orderNumber = 123L;

        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            orderServiceImpl.returnOrder(orderNumber, LocalDate.now());
        });

        verify(orderRepository, times(1)).findOrderByOrderNumber(orderNumber);
    }

    @Test
    void testReturnOrder_BookNotFound() {
        Long orderNumber = 123L;
        Long bookId = 1L;
        List<Long> bookIds = List.of(bookId);

        Order order = new Order();
        Book book = new Book();
        book.setId(bookId);
        order.setBooks(List.of(book));

        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(order);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            orderServiceImpl.returnOrder(orderNumber, LocalDate.now());
        });

        verify(orderRepository, times(1)).findOrderByOrderNumber(orderNumber);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testReturnOrder_InternalServerError() {
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

        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");

        Long orderNumber = 123L;
        Long bookId = 1L;
        List<Long> bookIds = List.of(bookId);

        Order order = new Order();

        Book book = new Book();
        book.setId(1L);
        book.setIsbn("hry66fhd7j");
        book.setName("The da Vinci Code");
        book.setStockCount(10L);
        book.setPublishedYear("1987");
        book.setAuthors(List.of(author));
        book.setStudents(List.of(student));
        book.setCategory(category);
        book.setOrders(List.of(order));
        book.setImage("https://s3.com/test.jpg");

        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(order);

        doThrow(new RuntimeException("Database error")).when(orderRepository).save(any(Order.class));

        ResponseEntity<OrderDTO> response = orderServiceImpl.returnOrder(orderNumber, LocalDate.now());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(orderRepository, times(1)).findOrderByOrderNumber(orderNumber);
    }

    @Test
    void testReturnOrder_Success() {
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

        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");

        Long orderNumber = 123L;
        Long bookId = 1L;
        List<Long> bookIds = List.of(bookId);

        Order order = new Order();

        Book book = new Book();
        book.setId(1L);
        book.setIsbn("hry66fhd7j");
        book.setName("The da Vinci Code");
        book.setStockCount(10L);
        book.setPublishedYear("1987");
        book.setAuthors(List.of(author));
        book.setStudents(List.of(student));
        book.setCategory(category);
        book.setOrders(List.of(order));
        book.setImage("https://s3.com/test.jpg");

        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(order);

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.saveAll(anyList())).thenReturn(List.of(book));

        ResponseEntity<OrderDTO> response = orderServiceImpl.returnOrder(orderNumber, LocalDate.now());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(bookRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdateOrder() {
        Long orderId = 1L;
        Long orderNumber = 12345L;
        LocalDate localDate = LocalDate.now();
        LocalDate deliveryDate = LocalDate.now().plusDays(5);
        Long studentId = 1L;
        List<Long> bookIds = List.of(1L, 2L);

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Book 1");
        book1.setStockCount(10L);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Book 2");
        book2.setStockCount(5L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book2));

        Student student = new Student();
        student.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Order order = new Order();
        order.setId(orderId);
        order.setOrderNumber(123L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ResponseEntity<Order> response = orderServiceImpl.update(orderId, orderNumber, localDate, deliveryDate, bookIds, studentId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Order updatedOrder = response.getBody();
        assertNotNull(updatedOrder);
        assertEquals(orderNumber, updatedOrder.getOrderNumber());
        assertEquals(localDate, updatedOrder.getLocalDate());
        assertEquals(deliveryDate, updatedOrder.getDeliveryTime());
        assertEquals(2, updatedOrder.getBooks().size());
        assertEquals(studentId, updatedOrder.getStudent().getId());

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(2L);
        verify(studentRepository, times(1)).findById(studentId);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(updatedOrder);
    }

    @Test
    void testGetAllOrders() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderNumber(12345L);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderNumber(67890L);

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findAll()).thenReturn(orders);

        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setOrderNumber(12345L);
        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setOrderNumber(67890L);
        when(orderMapper.orderToOrderDTO(order1)).thenReturn(orderDTO1);
        when(orderMapper.orderToOrderDTO(order2)).thenReturn(orderDTO2);

        ResponseEntity<List<OrderDTO>> response = orderServiceImpl.getAllOrders();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<OrderDTO> orderDTOS = response.getBody();
        assertNotNull(orderDTOS);
        assertEquals(2, orderDTOS.size());
        assertEquals(12345L, orderDTOS.get(0).getOrderNumber());
        assertEquals(67890L, orderDTOS.get(1).getOrderNumber());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrders_NotFound() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> orderServiceImpl.getAllOrders());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetById_OrderFound() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNumber(12345L);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(12345L);
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderServiceImpl.getById(orderId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(12345L, response.getBody().getOrderNumber());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetById_OrderNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        ResponseEntity<OrderDTO> response = orderServiceImpl.getById(orderId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetByOrderName_OrderFound() {
        Long orderNumber = 1L;

        Order mockOrder = new Order();
        mockOrder.setOrderNumber(orderNumber);
        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(mockOrder);

        ResponseEntity<OrderDTO> response = orderServiceImpl.getByOrderName(orderNumber);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(orderRepository, times(1)).findOrderByOrderNumber(orderNumber);
    }

    @Test
    void testGetByOrderName_OrderNotFound() {
        Long orderNumber = 1L;

        when(orderRepository.findOrderByOrderNumber(orderNumber)).thenReturn(null);

        ResponseEntity<OrderDTO> response = orderServiceImpl.getByOrderName(orderNumber);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(orderRepository, times(1)).findOrderByOrderNumber(orderNumber);
    }

    @Test
    void testDelete_OrderNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = orderServiceImpl.delete(orderId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testDelete_OrderDeletedSuccessfully() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ResponseEntity<String> response = orderServiceImpl.delete(orderId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted!", response.getBody());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(order);
    }
}
