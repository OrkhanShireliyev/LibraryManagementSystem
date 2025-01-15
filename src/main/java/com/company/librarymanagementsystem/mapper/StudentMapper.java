package com.company.librarymanagementsystem.mapper;

import com.company.librarymanagementsystem.dto.StudentDTO;
import com.company.librarymanagementsystem.model.Student;
import com.company.librarymanagementsystem.request.StudentRequest;
import org.mapstruct.Mapper;

@Mapper
public interface StudentMapper {

    Student studentRequestToStudent(StudentRequest studentRequest);

    StudentDTO studentToStudentDTO(Student student);
}
