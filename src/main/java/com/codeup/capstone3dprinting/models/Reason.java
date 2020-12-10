package com.codeup.capstone3dprinting.models;

import javax.persistence.*;

@Entity
@Table(name = "reasons")
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String reason_description;

}
