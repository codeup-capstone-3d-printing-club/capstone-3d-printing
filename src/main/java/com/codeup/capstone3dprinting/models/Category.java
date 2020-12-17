package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String category;

    @ManyToMany(mappedBy = "categories")
    private List<File> files;

    public Category() {
    }

    public Category(long id, String category) {
        this.id = id;
        this.category = category;
    }

    public Category(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
