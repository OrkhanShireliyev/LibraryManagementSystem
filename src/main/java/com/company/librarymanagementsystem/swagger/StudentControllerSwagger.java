package com.company.librarymanagementsystem.swagger;

import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.request.StudentRequest;
import com.company.librarymanagementsystem.service.inter.StudentServiceInter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Student Controller", description = "Operations related to student management")
public class StudentControllerSwagger {

    private final StudentServiceInter studentServiceInter;

    @Operation(summary = "Save student", description = "Fill student information and save it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved!"),
            @ApiResponse(responseCode = "500", description = "Can't save student!")
    })
    @PostMapping("/save/{bookIds}/{orderIds}")
    ResponseEntity<StudentDTO> save(@RequestBody StudentRequest studentRequest,
                                    @PathVariable(required = false) List<Long> bookIds,
                                    @PathVariable(required = false) List<Long> orderIds) {
        try {
            StudentDTO student = studentServiceInter.save(studentRequest, bookIds, orderIds).getBody();
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update student", description = "Fill student for change student's info and update it!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated!"),
            @ApiResponse(responseCode = "500", description = "Can't update student!")
    })
    @PutMapping("/update/{id}/{bookId}/{orderId}")
    ResponseEntity<StudentDTO> update(@PathVariable Long id,
                                      @RequestBody StudentRequest studentRequest,
                                      @PathVariable(required = false) List<Long> bookId,
                                      @PathVariable(required = false) List<Long> orderId
    ) {
        try {
            StudentDTO student = studentServiceInter.update(id, studentRequest, bookId, orderId).getBody();
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all students", description = "Get all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The student was not found")
    })
    @GetMapping("/students")
    ResponseEntity<List<StudentDTO>> getAllStudents() {
        try {
            List<StudentDTO> studentDTOS = studentServiceInter.getAllStudent().getBody();

            if (studentDTOS == null || studentDTOS.isEmpty()) {
                return ResponseEntity.ok().build();
            }
            return new ResponseEntity<>(studentDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a student by id", description = "Returns a student as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The student was not found")
    })
    @GetMapping("/getById/{id}")
    ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        try {
            StudentDTO studentDTO = studentServiceInter.getStudentById(id).getBody();
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a student by id", description = "Delete a student as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Can't delete student!")
    })
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            studentServiceInter.delete(id);
            return new ResponseEntity<>("Successfully deleted!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred deleting student!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
