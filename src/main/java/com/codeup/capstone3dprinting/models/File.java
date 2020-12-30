package com.codeup.capstone3dprinting.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @Column(name = "is_flagged", nullable = false)
    private boolean isFlagged;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "file")
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "file")
    private List<FileImage> img;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "file_category",
            joinColumns = {@JoinColumn(name = "file_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private List<Category> categories;

}
