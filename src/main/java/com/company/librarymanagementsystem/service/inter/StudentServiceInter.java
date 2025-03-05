package com.company.librarymanagementsystem.service.inter;

import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.request.StudentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentServiceInter {

    ResponseEntity<StudentDTO> save(StudentRequest studentRequest, List<Long> bookIds, List<Long> orderIds);

    ResponseEntity<StudentDTO> update(Long id,
                                   StudentRequest studentRequest,
                                   List<Long> bookId,
                                   List<Long> orderId);

    ResponseEntity<List<StudentDTO>> getAllStudent();

    ResponseEntity<StudentDTO> getStudentById(Long id);

    ResponseEntity<String> delete(Long id);

}
