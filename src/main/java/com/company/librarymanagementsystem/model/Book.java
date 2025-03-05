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
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String isbn;
    private String publishedYear;
    private String image;
    private Long stockCount;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
    private List<Author> authors=new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "category_id")
//    @JsonIgnore
    private Category category;

    @ManyToMany()
    @JoinTable(
            name = "book_student",
            joinColumns = @JoinColumn(name = "book_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore
    private List<Student> students;

    @ManyToMany(mappedBy = "books")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Order> orders;

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
