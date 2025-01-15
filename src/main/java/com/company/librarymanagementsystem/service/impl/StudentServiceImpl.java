package com.company.librarymanagementsystem.service.impl;

import com.company.librarymanagementsystem.dto.StudentDTO;
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
    public ResponseEntity<StudentRequest> save(StudentRequest studentRequest) {
        try{
          Student student=studentMapper.studentRequestToStudent(studentRequest);
          studentRepository.save(student);
          log.info("Successfully created{}",student);
          return new ResponseEntity<>(studentRequest, HttpStatus.OK);
         }catch (Exception e){
          log.error("Error occurred when creating student!");
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @Override
    public ResponseEntity<Student> update(Long id,
                                          String registryCode,
                                          String name, String surname,
                                          int age,
                                          List<Long> bookId,
                                          List<Long> orderId) {
        Student student=studentRepository.findById(id).get();
        if (student==null){
            throw new NoSuchElementException("Not found student by id="+id);
        }

        List<Book> books=new ArrayList<>();
        for (Long bookById:bookId){
            Book findBookById=bookRepository.findById(bookById).get();
            if (findBookById==null){
                throw new NoSuchElementException("Not found book by id="+bookById);
            }
            books.add(findBookById);
        }

        List<Order> orders=new ArrayList<>();
        for (Long orderById: orderId){
            Order findOrderById=orderRepository.findById(orderById).get();
            if (findOrderById==null){
                throw new NoSuchElementException("Not found order by id="+orderById);
            }
            orders.add(findOrderById);
        }

        try {
            student.setRegistryCode(registryCode);
            student.setName(name);
            student.setSurname(surname);
            student.setAge(age);
            student.setBooks(books);
            student.setOrders(orders);
            studentRepository.save(student);
            log.info("Successfully updated{}",student);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred when updating student!");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<StudentDTO>> getAllStudent() {
        List<Student> students=studentRepository.findAll();
        if(students==null){
            throw new NullPointerException("Not found students!");
        }

        StudentDTO studentDTO;
        List<StudentDTO> studentDTOS=new ArrayList<>();

        try{
            for(Student student: students){
                studentDTO=studentMapper.studentToStudentDTO(student);
                studentDTOS.add(studentDTO);
            }
            log.info("Successfully retrieved{}",studentDTOS);
            return new ResponseEntity<>(studentDTOS,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occured while retrieving students!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<StudentDTO> getStudentById(Long id) {
        Student student=studentRepository.findById(id).get();
        if(student==null){
            throw new NullPointerException("Not found student by id="+id);
        }
        StudentDTO studentDTO;
        try{
            studentDTO=studentMapper.studentToStudentDTO(student);
            log.info("Successfully retrieved{}",studentDTO);
            return new ResponseEntity<>(studentDTO,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occured while retrieving student by id="+id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        Student student=studentRepository.findById(id).get();
        if(student==null){
            throw new NullPointerException("Not found student by id="+id);
        }

        try{
            studentRepository.deleteById(id);
            log.info("Successfully deleted{}",student);
        return new ResponseEntity<>("Successfully deleted{"+student+"}",HttpStatus.OK);
    }catch (Exception e){
            log.error("Error occured while retrieving student by id="+id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
}
