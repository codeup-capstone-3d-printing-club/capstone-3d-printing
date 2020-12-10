package com.codeup.capstone3dprinting.models;

import javax.persistence.*;
@Entity
@Table(name="ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

    @OneToMany
    @JoinColumn(nullable = false)
    private User rater_id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private File file_id;

    @Column(nullable = false)
    private int rating;

}
