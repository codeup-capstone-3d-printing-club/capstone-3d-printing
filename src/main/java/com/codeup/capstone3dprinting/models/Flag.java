package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
@Entity
@Table(name="flags")
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    @JoinColumn(nullable = false)
    private File file_id;

    @OneToOne
    @JoinColumn(nullable = false)
    private User reporter_id;

    @Column(nullable = false)
    public enum reason{
        Inapropriate,
        WrongCategory,
        other
    }
}
