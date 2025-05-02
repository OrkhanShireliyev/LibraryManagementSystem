package com.company.librarymanagementsystem;

import com.company.librarymanagementsystem.mapper.StudentMapper;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.repository.BookRepository;
import com.company.librarymanagementsystem.repository.OrderRepository;
import com.company.librarymanagementsystem.repository.StudentRepository;
import com.company.librarymanagementsystem.request.StudentRequest;
import com.company.librarymanagementsystem.service.impl.StudentServiceImpl;
import com.company.librarymanagementsystem.dto.StudentDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @Test
    void testSave_StudentCreatedSuccessfully() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("Book 1");
        book1.setStockCount(5L);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Book 2");
        book2.setStockCount(3L);

        List<Book> books = new ArrayList<>();

        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        List<Order> orders = new ArrayList<>();

        List<Long> bookIds = new ArrayList<>();
        bookIds.add(1L);
        bookIds.add(2L);

        List<Long> orderIds = new ArrayList<>();
        orderIds.add(1L);
        orderIds.add(2L);

        Student student = new Student();
        student.setId(1L);
        student.setRegistryCode("123");
        student.setName("John");
        student.setSurname("Doe");
        student.setAge(22);
        student.setOrders(orders);
        student.setBooks(books);

        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setRegistryCode("123");
        studentRequest.setName("John");
        studentRequest.setSurname("Doe");
        student.setAge(22);
        student.setOrders(orders);
        student.setBooks(books);

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setRegistryCode("123");
        studentDTO.setName("John");
        studentDTO.setSurname("Doe");
        student.setAge(22);
        student.setOrders(orders);
        student.setBooks(books);


        book1.setStudents(List.of(student));
        book2.setStudents(List.of(student));

        books.add(book1);
        books.add(book2);

        order1.setStudent(student);
        order2.setStudent(student);

        orders.add(order1);
        orders.add(order2);

        when(bookRepository.findAllById(bookIds)).thenReturn(books);
        when(orderRepository.findAllById(orderIds)).thenReturn(orders);
        when(studentMapper.studentRequestToStudent(studentRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.studentToStudentDTO(student)).thenReturn(studentDTO);

        ResponseEntity<StudentDTO> response = studentServiceImpl.save(studentRequest, bookIds, orderIds);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookRepository, times(1)).findAllById(bookIds);
        verify(orderRepository, times(1)).findAllById(orderIds);
//        verify(studentMapper, times(1)).studentRequestToStudent(studentRequest);
        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).studentToStudentDTO(student);
    }


    @Test
    void testSave_ErrorOccurred() {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName("Test Student");

        List<Long> bookIds=new ArrayList<>();
        bookIds.add(1L);
        bookIds.add(2L);

        List<Long> orderIds = new ArrayList<>();
        orderIds.add(1L);
        orderIds.add(2L);
        when(bookRepository.findAllById(bookIds)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<StudentDTO> response = studentServiceImpl.save(studentRequest, bookIds, orderIds);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(bookRepository, times(1)).findAllById(bookIds);
    }

    @Test
    void testUpdate_StudentUpdatedSuccessfully() {
        Long studentId = 1L;

        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setName("Old Name");
        existingStudent.setBooks(new ArrayList<>());
        existingStudent.setOrders(new ArrayList<>());

        Book book1 = new Book();
        book1.setId(1L);
        book1.setStudents(new ArrayList<>());
        Book book2 = new Book();
        book2.setId(2L);
        book2.setStudents(new ArrayList<>());
        List<Book> books = Arrays.asList(book1, book2);

        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        List<Order> orders = Arrays.asList(order1, order2);

        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setRegistryCode("RC-123");
        studentRequest.setName("New Name");
        studentRequest.setSurname("New Surname");
        studentRequest.setAge(22);

        List<Long> bookIds = Arrays.asList(1L, 2L);
        List<Long> orderIds = Arrays.asList(1L, 2L);

        when(bookRepository.findAllById(bookIds)).thenReturn(books);
        when(orderRepository.findAllById(orderIds)).thenReturn(orders);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(studentMapper.studentToStudentDTO(any(Student.class))).thenReturn(new StudentDTO());

        ResponseEntity<StudentDTO> response = studentServiceImpl.update(studentId, studentRequest, bookIds, orderIds);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(studentRepository).findById(studentId);
        verify(bookRepository).findAllById(bookIds);
        verify(orderRepository).findAllById(orderIds);
        verify(studentRepository).save(any(Student.class));
        verify(bookRepository).saveAll(books);
        verify(orderRepository).saveAll(orders);
        verify(studentMapper).studentToStudentDTO(any(Student.class));
    }
    @Test
    void testGetAllStudent() {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("John");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Alice");

        List<Student> students = Arrays.asList(student1, student2);

        StudentDTO dto1 = new StudentDTO();
        dto1.setId(1L);
        dto1.setName("John");

        StudentDTO dto2 = new StudentDTO();
        dto2.setId(2L);
        dto2.setName("Alice");

        when(studentRepository.findAll()).thenReturn(students);
        when(studentMapper.studentToStudentDTO(student1)).thenReturn(dto1);
        when(studentMapper.studentToStudentDTO(student2)).thenReturn(dto2);

        ResponseEntity<List<StudentDTO>> response = studentServiceImpl.getAllStudent();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getName());

        verify(studentRepository).findAll();
        verify(studentMapper).studentToStudentDTO(student1);
        verify(studentMapper).studentToStudentDTO(student2);
    }
    @Test
    void testGetStudentById() {
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setName("Test Student");

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(studentId);
        studentDTO.setName("Test Student");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.studentToStudentDTO(student)).thenReturn(studentDTO);

        ResponseEntity<StudentDTO> response = studentServiceImpl.getStudentById(studentId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(studentId, response.getBody().getId());
        assertEquals("Test Student", response.getBody().getName());

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapper, times(1)).studentToStudentDTO(student);
    }

    @Test
    void testDeleteStudent() {
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setBooks(new ArrayList<>());
        student.setOrders(new ArrayList<>());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        ResponseEntity<String> response = studentServiceImpl.delete(studentId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Successfully deleted"));

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(student);
    }

}
