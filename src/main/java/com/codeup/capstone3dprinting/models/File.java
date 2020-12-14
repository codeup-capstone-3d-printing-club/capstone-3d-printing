package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 500)
    private String file_url;

    @Column(nullable = false, length = 100)
    private String file_title;

    @Column(nullable = false)
    private Timestamp created_at;

    @Column(nullable = false)
    private Timestamp updated_at;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column
    private String img_url;

    @Column(nullable = false)
    private boolean is_private;

    @OneToOne
    private User owner;

    //Create
    public File(long id, String file_url, String file_title, Timestamp created_at,
                Timestamp updated_at, String description, String img_url, boolean is_private, User owner) {
        this.id = id;
        this.file_url = file_url;
        this.file_title = file_title;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.description = description;
        this.img_url = img_url;
        this.is_private = is_private;
        this.owner = owner;
    }

    public File() {
    }

    //Read
    public File(String file_url, String file_title, Timestamp created_at,
                Timestamp updated_at, String description, String img_url, boolean is_private, User owner) {
        this.file_url = file_url;
        this.file_title = file_title;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.description = description;
        this.img_url = img_url;
        this.is_private = is_private;
        this.owner = owner;
    }

    public File(File copy) {
        id = copy.id;
        file_url = copy.file_url;
        file_title = copy.file_title;
        created_at = copy.created_at;
        updated_at = copy.updated_at;
        description = copy.description;
        img_url = copy.img_url;
        is_private = copy.is_private;
        owner = copy.owner;
    }


}
