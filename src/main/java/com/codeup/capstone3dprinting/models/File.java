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

}
