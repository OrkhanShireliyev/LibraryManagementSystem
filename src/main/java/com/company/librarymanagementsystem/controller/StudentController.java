package com.company.librarymanagementsystem.controller;

import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.request.StudentRequest;
import com.company.librarymanagementsystem.service.inter.StudentServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceInter studentServiceInter;

    @PostMapping("/save")
    String save(@ModelAttribute StudentRequest studentRequest){
        studentServiceInter.save(studentRequest);
        return "save";
    }

    @PostMapping("/update/{id}/{registryCode}/{name}/{surname}/{age}/{bookId}/{orderId}")
    String update(@PathVariable Long id,
                  @PathVariable String registryCode,
                  @PathVariable String name,
                  @PathVariable String surname,
                  @PathVariable int age,
                  @PathVariable List<Long> bookId,
                  @PathVariable List<Long> orderId,
                  Model model){
        Student student=studentServiceInter.update(id, registryCode, name, surname, age, bookId, orderId).getBody();
        model.addAttribute("update",student);
        return "update";
    }

    @PostMapping("/students")
    String getAllStudent(Model model){
        List<StudentDTO> studentDTOS=studentServiceInter.getAllStudent().getBody();
        model.addAttribute("students",studentDTOS);
        return "students";
    }

    @PostMapping("/getById/{id}")
    String getStudentById(@PathVariable Long id,Model model){
        StudentDTO studentDTO=studentServiceInter.getStudentById(id).getBody();
        model.addAttribute("getById",studentDTO);
        return "getById";
    }

    @DeleteMapping("/delete/{id}")
    String delete(@PathVariable Long id,Model model){
        StudentDTO studentDTO=studentServiceInter.getStudentById(id).getBody();
        studentServiceInter.delete(id);
        model.addAttribute("successMessage", "Student successfully deleted!");
        return "delete";
    }
}
