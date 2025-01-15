package com.company.librarymanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String registryCode;
    private String name;
    private String surname;
    private int age;

    @ManyToMany(mappedBy = "students")
    private List<Book> books;

    @OneToMany(mappedBy = "student")
    private List<Order> orders;
}

