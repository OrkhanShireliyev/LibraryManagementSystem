package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.exception.NotFoundException;
import com.company.librarymanagementsystem.mapper.StudentMapper;
import com.company.librarymanagementsystem.model.Book;
import com.company.librarymanagementsystem.model.Order;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.repository.BookRepository;
import com.company.librarymanagementsystem.repository.OrderRepository;
import com.company.librarymanagementsystem.repository.StudentRepository;
import com.company.librarymanagementsystem.request.StudentRequest;
import com.company.librarymanagementsystem.service.inter.StudentServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentServiceInter {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    private final BookRepository bookRepository;

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public ResponseEntity<StudentDTO> save(StudentRequest studentRequest, List<Long> bookIds, List<Long> orderIds) {
        List<Book> books = bookRepository.findAllById(bookIds);

        List<Order> orders = orderRepository.findAllById(orderIds);

        try {
            Student student = studentMapper.studentRequestToStudent(studentRequest);

            for (Book book : books) {
                book.getStudents().add(student);
            }

            for (Order order : orders) {
                order.setStudent(student);
            }

            student.setBooks(books);
            student.setOrders(orders);
            studentRepository.save(student);
            StudentDTO studentDTO=studentMapper.studentToStudentDTO(student);
            log.info("Successfully created{}", student);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when creating student!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<StudentDTO> update(Long id,
                                             StudentRequest studentRequest,
                                             List<Long> bookIds,
                                             List<Long> orderIds) {

        List<Book> books = bookRepository.findAllById(bookIds);

        List<Order> orders = orderRepository.findAllById(orderIds);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found student by id=" + id));

        try {
            for (Book book : student.getBooks()) {
                book.getStudents().remove(student);
            }
            for (Order order : student.getOrders()) {
                order.setStudent(null);
            }

            student.getBooks().clear();
            student.getOrders().clear();

            for (Book book : books) {
                book.getStudents().add(student);
            }

            for (Order order : orders) {
                order.setStudent(student);
            }

            student.setRegistryCode(studentRequest.getRegistryCode());
            student.setName(studentRequest.getName());
            student.setSurname(studentRequest.getSurname());
            student.setAge(studentRequest.getAge());
            student.setBooks(books);
            student.setOrders(orders);
            studentRepository.save(student);
            bookRepository.saveAll(books);
            orderRepository.saveAll(orders);

            StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
            log.info("Successfully updated {}", student);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred when updating student!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<List<StudentDTO>> getAllStudent() {
        List<Student> students = studentRepository.findAll();
        if (students == null || students.isEmpty()) {
            throw new NoSuchElementException("Not found students!");
        }

        try {
            List<StudentDTO> studentDTOS = new ArrayList<>();
            for (Student student : students) {
                StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
                studentDTOS.add(studentDTO);
            }
            log.info("Successfully retrieved{}", studentDTOS);
            return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occured while retrieving students!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<StudentDTO> getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found student by id=" + id));

        try {
            StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
            log.info("Successfully retrieved{}", studentDTO);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occured while retrieving student by id=" + id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found student by id=" + id));
        try {
            for (Book book : student.getBooks()) {
                book.getStudents().remove(student);
            }
            for (Order order : student.getOrders()) {
                order.setStudent(null);
            }
            student.getBooks().clear();
            student.getOrders().clear();

            studentRepository.delete(student);
            log.info("Successfully deleted {}", student);
            return new ResponseEntity<>("Successfully deleted{" + student + "}", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occured while retrieving student by id=" + id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
