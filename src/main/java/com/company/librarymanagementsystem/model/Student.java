package com.company.librarymanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "orders")
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

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", registryCode='" + registryCode + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", books=" + books +
                ", orders=" + (orders != null ? orders.size() : 0) +
                '}';
    }
}

