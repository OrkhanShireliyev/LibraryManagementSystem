package com.company.librarymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Book> books=new ArrayList<>();

    @Override
    public String toString() {
        return "Author{id='" +id+ "', name='" + name + "', surname='" + surname + "', books=" + books + "}";
    }
}
