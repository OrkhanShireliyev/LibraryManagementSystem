package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.request.StudentRequest;
import com.company.librarymanagementsystem.service.inter.StudentServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentServiceInter studentServiceInter;

    @GetMapping("/")
    String getStudentPage() {
        return "student";
    }

    @PostMapping("/save")
    ResponseEntity<StudentDTO> save(@RequestBody StudentRequest studentRequest,
                                    @RequestParam(value = "bookIds", required = false, defaultValue = "") String bookIds,
                                    @RequestParam(value = "orderIds", required = false, defaultValue = "") String orderIds) {
        try {
            System.out.println("Received bookIds: " + bookIds);
            System.out.println("Received orderIds: " + orderIds);

            List<Long> bookListId = (bookIds != null && !bookIds.trim().isEmpty())
                    ? Arrays.stream(bookIds.split(",")).map(Long::parseLong).collect(Collectors.toList())
                    : Collections.emptyList();

            List<Long> orderListIds = (orderIds != null && !orderIds.trim().isEmpty())
                    ? Arrays.stream(orderIds.split(",")).map(Long::parseLong).collect(Collectors.toList())
                    : Collections.emptyList();

            StudentDTO student = studentServiceInter.save(studentRequest, bookListId, orderListIds).getBody();
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{studentId}")
    ResponseEntity<StudentDTO> update(@PathVariable Long id,
                                      @RequestBody StudentRequest studentRequest,
                                      @RequestParam(value = "orderIds", required = false, defaultValue = "") String bookIds,
                                      @RequestParam(value = "orderIds", required = false, defaultValue = "") String orderIds
    ) {
        try {
            System.out.println("Received bookIds: " + bookIds);
            System.out.println("Received orderIds: " + orderIds);

            List<Long> bookListId = (!bookIds.isEmpty())
                    ? Arrays.stream(bookIds.split(",")).map(Long::parseLong).collect(Collectors.toList())
                    : Collections.emptyList();

            List<Long> orderListIds = (!orderIds.isEmpty())
                    ? Arrays.stream(orderIds.split(",")).map(Long::parseLong).collect(Collectors.toList())
                    : Collections.emptyList();

            StudentDTO student = studentServiceInter.update(id, studentRequest, bookListId, orderListIds).getBody();
            return ResponseEntity.status(HttpStatus.OK).body(student);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/students")
    ResponseEntity<List<StudentDTO>> getAllStudents() {
        try {
            List<StudentDTO> studentDTOS = studentServiceInter.getAllStudent().getBody();
            if (studentDTOS == null || studentDTOS.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.status(HttpStatus.OK).body(studentDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id, Model model) {
        try {
            StudentDTO studentDTO = studentServiceInter.getStudentById(id).getBody();
            model.addAttribute("getById", studentDTO);
            return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id, Model model) {
        try {
            studentServiceInter.delete(id);
            model.addAttribute("successMessage", "Student successfully deleted!");
            return ResponseEntity.status(HttpStatus.OK).body("Student successfully deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred deleting student!");
        }
    }
}
