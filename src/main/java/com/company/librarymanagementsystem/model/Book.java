package com.company.librarymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String isbn;
    private String publishedYear;
    private String image;
    private Long stockCount;

    @ManyToMany(mappedBy = "books")
//    @JoinTable(
//            name = "book_author",
//            joinColumns = @JoinColumn(name = "book_id"),
//            inverseJoinColumns = @JoinColumn(name = "author_id")
//    )
    @JsonIgnore
    private List<Author> authors=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "book_student",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    @ManyToOne()
    @JoinColumn(name = "orders_id")
    private Order order;

    @Override
    public String toString() {
        return "Book{name='" + name
                + "', isbn='" + isbn
                + "', publishedYear='" + publishedYear
                + "', image='"+image
                + "', stockCount'"+stockCount
                + "}";
    }
}
