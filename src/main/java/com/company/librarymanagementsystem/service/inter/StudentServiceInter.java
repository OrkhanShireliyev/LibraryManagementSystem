package com.company.librarymanagementsystem.service.inter;

import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.request.StudentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentServiceInter{

    ResponseEntity<StudentRequest> save(StudentRequest studentRequest);

    ResponseEntity<Student> update(Long id,
                                   String registryCode,
                                   String name,
                                   String surname,
                                   int age,
                                   List<Long> bookId,
                                   List<Long> orderId);

    ResponseEntity<List<StudentDTO>> getAllStudent();

    ResponseEntity<StudentDTO> getStudentById(Long id);

    ResponseEntity<String> delete(Long id);

}
